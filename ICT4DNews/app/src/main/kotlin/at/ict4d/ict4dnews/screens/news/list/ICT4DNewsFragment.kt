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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIctdnewsListBinding
import at.ict4d.ict4dnews.extensions.moveToTop
import at.ict4d.ict4dnews.extensions.visible
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.util.ScrollToTopRecyclerViewScrollHandler
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ICT4DNewsFragment : BaseFragment<ICT4DNewsViewModel, FragmentIctdnewsListBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_ictdnews_list

    override fun getViewModel(): Class<ICT4DNewsViewModel> = ICT4DNewsViewModel::class.java

    private val adapter: ICT4DNewsRecyclerViewAdapter = ICT4DNewsRecyclerViewAdapter { pair, view ->
        val action = ICT4DNewsFragmentDirections.ActionActionNewsToICT4DNewsDetailActivity(pair.first)
        view.findNavController().navigate(action)
    }

    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        model.isRefreshing.observe(this, Observer {
            binding.swiperefresh.isRefreshing = it ?: false
        })

        binding.swiperefresh.setOnRefreshListener {
            model.requestToLoadFeedsFromServers(true)
        }

        model.activeBlogsCount.observe(this, Observer {
            val updateText = model.getNewsLoadingText(it.peekContent(), resources)
            it.getContentIfNotHandled()?.let { _ -> activity?.toast(updateText) }

            if (model.newsList.value == null || model.newsList.value?.isEmpty() == true) {
                binding.progressTextView.text = updateText
                binding.progressTextView.visible(true)
            }
        })

        model.searchedNewsList.observe(this, Observer {
            if (it != null) {
                Timber.d("Search result size is ----> ${it.size} and query is ----> ${model.searchQuery}")
                adapter.submitList(it)
            }
        })

        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = adapter

        model.newsList.observe(this, Observer {
            if (it != null && it.isNotEmpty() && model.searchQuery.isNullOrBlank()) {
                Timber.d("list in fragment: ${it.size}")
                binding.progressTextView.visible(false)
                adapter.submitList(it)
                if (model.isRefreshing.value == false) {
                    binding.recyclerview.moveToTop()
                }
            }
        })

        binding.quickScroll.setOnClickListener { binding.recyclerview.moveToTop() }
        binding.recyclerview.addOnScrollListener(
            ScrollToTopRecyclerViewScrollHandler(
                binding.quickScroll
            )
        )

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        this.menu = menu
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
                model.performSearch(query)
            }, { e ->
                Timber.e("$e")
            }, {
                Timber.d("search complete")
            })
        )

        // restore search view after orientation change
        model.searchQuery?.let {
            if (it.isNotEmpty()) {
                enableRefreshMenuItem(false)
                menuItem.expandActionView()
                searchView.setQuery(it, true)
                searchView.clearFocus()
            }
        }

        menuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                enableRefreshMenuItem(false)
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                adapter.submitList(model.searchedNewsList.value)
                model.searchQuery = null
                enableRefreshMenuItem(true)
                if (model.isRefreshing.value == true) {
                    binding.swiperefresh.isRefreshing = false
                    binding.swiperefresh.isRefreshing = true
                }
                return true
            }
        })
    }

    private fun enableRefreshMenuItem(enable: Boolean) {
        menu?.findItem(R.id.menu_refresh)?.isEnabled = enable
        binding.swiperefresh.isEnabled = enable
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {

            R.id.menu_refresh -> {
                model.requestToLoadFeedsFromServers(true)
                return true
            }

            R.id.menu_filter -> {
                view?.findNavController()
                    ?.navigate(ICT4DNewsFragmentDirections.actionActionNewsToBlogAndSourceFragment())
                return true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}