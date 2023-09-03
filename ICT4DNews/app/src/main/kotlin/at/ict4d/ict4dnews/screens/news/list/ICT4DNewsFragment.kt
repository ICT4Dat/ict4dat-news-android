package at.ict4d.ict4dnews.screens.news.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIctdnewsListBinding
import at.ict4d.ict4dnews.extensions.browseCustomTabWithUrl
import at.ict4d.ict4dnews.extensions.moveToTop
import at.ict4d.ict4dnews.extensions.navigateSafe
import at.ict4d.ict4dnews.extensions.queryTextChanges
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.util.ScrollToTopRecyclerViewScrollHandler
import at.ict4d.ict4dnews.server.utils.Status
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb
import at.ict4d.ict4dnews.utils.recordNavigationBreadcrumb
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class ICT4DNewsFragment : BaseFragment<FragmentIctdnewsListBinding>(
    R.layout.fragment_ictdnews_list
) {

    private val model by viewModel<ICT4DNewsViewModel>()

    private val adapter: ICT4DNewsRecyclerViewAdapter = ICT4DNewsRecyclerViewAdapter({ pair, _ ->
        recordNavigationBreadcrumb(
            "item click",
            this,
            mapOf("pair" to "$pair")
        )
        requireActivity().browseCustomTabWithUrl(pair.first.link)
    })

    private var activeBlogCount = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = adapter

        model.activeBlogsCount.observe(viewLifecycleOwner) { activeBlogCount ->
            this.activeBlogCount = activeBlogCount
        }

        adapter.mostRecentNewsPublishDateTime = model.lastAutomaticNewsUpdateLocalDate.get().atStartOfDay()

        lifecycleScope.launch {
            val blogsCount = model.blogsCount.first()

            if (blogsCount == 0 && model.isWelcomeSetupNotStartedOnce) { // no Blogs exist yet --> show Welcome Setup to download them
                model.isWelcomeSetupNotStartedOnce = false
                findNavController().navigateSafe(
                    R.id.newsListFragment,
                    ICT4DNewsFragmentDirections.actionActionNewsToWelcomeSetupFragment()
                )
            } else {
                model.newsUpdateStatus.observe(viewLifecycleOwner) { newsUpdateResource ->

                    binding.swiperefresh.isRefreshing = newsUpdateResource.status == Status.LOADING
                    binding.progressTextView.isVisible = newsUpdateResource.status == Status.LOADING

                    newsUpdateResource.currentItem?.data?.name.let { currentBlogName ->
                        val progressText = getString(
                            R.string.connecting_text,
                            newsUpdateResource.successfulBlogs.count() + newsUpdateResource.failedBlogs.count() + 1,
                            newsUpdateResource.totalCount,
                            currentBlogName
                        )

                        binding.progressTextView.text = if (BuildConfig.DEBUG) {
                            "$progressText\n\nSuccessful: ${newsUpdateResource.successfulBlogs.count()}\nFailed:${newsUpdateResource.failedBlogs.count()}"
                        } else {
                            progressText
                        }
                    }

                    if (newsUpdateResource.status != Status.LOADING && BuildConfig.DEBUG) {
                        val failedBlogs = newsUpdateResource.failedBlogs.joinToString(separator = "\n") { it.name }

                        MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Debug Failed Blogs")
                            .setMessage("Fails:\n$failedBlogs")
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }

                binding.swiperefresh.setOnRefreshListener {
                    recordActionBreadcrumb("swipe-to-refresh", this)
                    model.requestToLoadFeedsFromServers(true)
                }

                binding.swiperefresh.isRefreshing = true
                model.newsList.observe(viewLifecycleOwner) {
                    Timber.d("list in fragment: ${it.size} ---- ${model.searchQuery}")

                    binding.swiperefresh.isRefreshing = model.isNewsUpdateLoading()
                    binding.recyclerview.isVisible = true
                    adapter.submitList(it)

                    if (model.shouldMoveScrollToTop) {
                        binding.recyclerview.moveToTop()
                        model.shouldMoveScrollToTop = false
                    }

                    if (it.isEmpty() && model.searchQuery.value?.isEmpty() == true) {
                        binding.recyclerview.isVisible = false
                    } else {
                        binding.nothingFound.isVisible = it.isEmpty()
                    }
                }

                binding.quickScroll.setOnClickListener {
                    recordActionBreadcrumb("quickscroll", this)
                    binding.recyclerview.moveToTop()
                }

                binding.recyclerview.addOnScrollListener(
                    ScrollToTopRecyclerViewScrollHandler(
                        binding.quickScroll
                    )
                )
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Add menu items here
                    menuInflater.inflate(R.menu.ict4dnews_menu, menu)
                    val menuItem = menu.findItem(R.id.menu_search)
                    val searchView = menuItem?.actionView as SearchView

                    searchView.queryTextChanges()
                        .drop(1)
                        .debounce(400)
                        .map { char: CharSequence -> char.toString() }
                        .onEach { query ->
                            try {
                                Timber.d("query: $query")
                                recordActionBreadcrumb("search", this, mapOf("query" to query))
                                model.performSearch(query)
                            } catch (e: Exception) {
                                Timber.e(e)
                            }
                        }.launchIn(lifecycleScope)

                    // restore search view after orientation change
                    if (model.searchQuery.value?.isNotEmpty() == true) {
                        enableRefreshMenuItem(false, menu)
                        menuItem.expandActionView()
                        searchView.setQuery(model.searchQuery.value, true)
                        searchView.clearFocus()
                    }

                    menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {

                        override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                            enableRefreshMenuItem(false, menu)
                            return true
                        }

                        override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                            adapter.submitList(model.newsList.value)
                            model.performSearch("")
                            enableRefreshMenuItem(true, menu)
                            if (model.isNewsUpdateLoading()) {
                                binding.swiperefresh.isRefreshing = false
                                binding.swiperefresh.isRefreshing = true
                            }
                            return true
                        }
                    })
                }

                override fun onMenuItemSelected(menuItem: MenuItem) = when (menuItem.itemId) {
                    R.id.menu_refresh -> {
                        model.requestToLoadFeedsFromServers(true)
                        true
                    }

                    R.id.menu_filter -> {
                        recordNavigationBreadcrumb("news list", this)
                        findNavController().navigateSafe(
                            R.id.newsListFragment,
                            ICT4DNewsFragmentDirections.actionActionNewsToBlogAndSourceFragment(false)
                        )
                        true
                    }

                    else -> false
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }

    private fun enableRefreshMenuItem(enable: Boolean, menu: Menu?) {
        menu?.findItem(R.id.menu_refresh)?.isEnabled = enable
        binding.swiperefresh.isEnabled = enable
    }
}
