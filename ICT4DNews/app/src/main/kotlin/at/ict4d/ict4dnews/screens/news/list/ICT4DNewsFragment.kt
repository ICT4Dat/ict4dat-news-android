package at.ict4d.ict4dnews.screens.news.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIctdnewsListBinding
import at.ict4d.ict4dnews.extensions.moveToTop
import at.ict4d.ict4dnews.extensions.navigateSafe
import at.ict4d.ict4dnews.extensions.setVisible
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.util.ScrollToTopRecyclerViewScrollHandler
import at.ict4d.ict4dnews.server.utils.Status
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb
import at.ict4d.ict4dnews.utils.recordNavigationBreadcrumb
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ICT4DNewsFragment : BaseFragment<ICT4DNewsViewModel, FragmentIctdnewsListBinding>(
    R.layout.fragment_ictdnews_list,
    ICT4DNewsViewModel::class
) {

    private val adapter: ICT4DNewsRecyclerViewAdapter = ICT4DNewsRecyclerViewAdapter({ pair, _ ->

        recordNavigationBreadcrumb("item click", this, mapOf("pair" to "$pair"))

        val action =
            ICT4DNewsFragmentDirections.actionActionNewsToICT4DNewsDetailFragment(pair.first)

        findNavController().navigateSafe(R.id.newsListFragment, action)
    })

    private var activeBlogCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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

            if (blogsCount == 0 && model.isSplashNotStartedOnce) { // no Blogs exist yet --> show Splash to download them
                model.isSplashNotStartedOnce = false
                findNavController().navigateSafe(
                    R.id.newsListFragment,
                    ICT4DNewsFragmentDirections.actionActionNewsToSplashFragment()
                )
            } else {

                model.newsUpdateStatus.observe(viewLifecycleOwner) { newsUpdateResource ->

                    binding.swiperefresh.isRefreshing = newsUpdateResource.status == Status.LOADING
                    binding.progressTextView.setVisible(newsUpdateResource.status == Status.LOADING)

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
                    binding.recyclerview.setVisible(true)
                    adapter.submitList(it)

                    if (model.shouldMoveScrollToTop) {
                        binding.recyclerview.moveToTop()
                        model.shouldMoveScrollToTop = false
                    }

                    if (it.isEmpty() && model.searchQuery.value?.isEmpty() == true) {
                        binding.recyclerview.setVisible(false)
                    } else {
                        binding.nothingFound.setVisible(it.isEmpty())
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
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.ict4dnews_menu, menu)
        val menuItem = menu.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView as SearchView

        compositeDisposable.add(RxSearchView.queryTextChanges(searchView)
            .debounce(400, TimeUnit.MILLISECONDS)
            .map { char: CharSequence -> char.toString() }
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ query ->
                Timber.d("query: $query")
                recordActionBreadcrumb("search", this, mapOf("query" to query))
                model.performSearch(query)
            }, { e ->
                Timber.e("$e")
            }, {
                Timber.d("search complete")
            })
        )

        // restore search view after orientation change
        if (model.searchQuery.value?.isNotEmpty() == true) {
            enableRefreshMenuItem(false, menu)
            menuItem.expandActionView()
            searchView.setQuery(model.searchQuery.value, true)
            searchView.clearFocus()
        }

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                enableRefreshMenuItem(false, menu)
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
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

    private fun enableRefreshMenuItem(enable: Boolean, menu: Menu?) {
        menu?.findItem(R.id.menu_refresh)?.isEnabled = enable
        binding.swiperefresh.isEnabled = enable
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_refresh -> {
                model.requestToLoadFeedsFromServers(true)
                return true
            }

            R.id.menu_filter -> {
                recordNavigationBreadcrumb("news list", this)
                findNavController().navigateSafe(
                    R.id.newsListFragment,
                    ICT4DNewsFragmentDirections.actionActionNewsToBlogAndSourceFragment()
                )
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}
