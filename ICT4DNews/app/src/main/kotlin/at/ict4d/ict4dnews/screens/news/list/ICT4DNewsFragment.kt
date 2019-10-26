package at.ict4d.ict4dnews.screens.news.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIctdnewsListBinding
import at.ict4d.ict4dnews.extensions.moveToTop
import at.ict4d.ict4dnews.extensions.navigateSafe
import at.ict4d.ict4dnews.extensions.visible
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.util.ScrollToTopRecyclerViewScrollHandler
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb
import at.ict4d.ict4dnews.utils.recordNavigationBreadcrumb
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import org.jetbrains.anko.toast
import timber.log.Timber

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = adapter

        model.activeBlogsCount.observe(
            this,
            Observer { activeBlogCount -> this.activeBlogCount = activeBlogCount })

        adapter.mostRecentNewsPublishDateTime =
            model.lastAutomaticNewsUpdateLocalDate.get().atStartOfDay()

        model.blogsCount.observe(this, Observer { blogsCount ->

            if (blogsCount == 0 && model.isSplashNotStartedOnce) { // no Blogs exist yet --> show Splash to download them
                model.isSplashNotStartedOnce = false
                findNavController().navigateSafe(
                    R.id.newsListFragment,
                    ICT4DNewsFragmentDirections.actionActionNewsToSplashFragment()
                )
            } else {

                model.isRefreshing.observe(this, Observer {
                    binding.swiperefresh.isRefreshing = it ?: false

                    if (it) {
                        val updateText = getNewsLoadingText(activeBlogCount)
                        if (model.newsList.value?.isEmpty() == true) {
                            binding.progressTextView.visible(true)
                        }
                        activity?.toast(updateText)
                    }
                })

                binding.swiperefresh.setOnRefreshListener {
                    recordActionBreadcrumb("swip-to-refresh", this)
                    model.requestToLoadFeedsFromServers(true)
                }

                model.newsList.observe(this, Observer {
                    if (it != null) {
                        Timber.d("list in fragment: ${it.size} ---- ${model.searchQuery}")
                        binding.recyclerview.visible(true)
                        binding.progressTextView.visible(false)
                        adapter.submitList(it)

                        if (model.shouldMoveScrollToTop) {
                            binding.recyclerview.moveToTop()
                            model.shouldMoveScrollToTop = false
                        }

                        if (it.isEmpty() && model.searchQuery.isEmpty()) {
                            val updateText = getNewsLoadingText(activeBlogCount)
                            binding.progressTextView.text = updateText
                            binding.progressTextView.visible(true)
                            binding.recyclerview.visible(false)
                        } else {
                            binding.nothingFound.visible(it.isEmpty())
                        }
                    }
                })

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
        })

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.ict4dnews_menu, menu)
        val menuItem = menu?.findItem(R.id.menu_search)
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
        if (model.searchQuery.isNotEmpty()) {
            enableRefreshMenuItem(false, menu)
            menuItem.expandActionView()
            searchView.setQuery(model.searchQuery, true)
            searchView.clearFocus()
        }

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                enableRefreshMenuItem(false, menu)
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                adapter.submitList(model.newsList.value)
                model.searchQuery = ""
                enableRefreshMenuItem(true, menu)
                if (model.isRefreshing.value == true) {
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

    private fun getNewsLoadingText(blogCount: Int): String {
        return if (blogCount == 0) {
            getString(R.string.no_blog_found)
        } else {
            String.format(getString(R.string.connecting_text), blogCount)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {

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
