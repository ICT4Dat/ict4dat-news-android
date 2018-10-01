package at.ict4d.ict4dnews.screens.news.list

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIctdnewsListBinding
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.util.ScrollToTopRecyclerViewScrollHandler
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.intentFor
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ICT4DNewsFragment : BaseFragment<ICT4DNewsViewModel, FragmentIctdnewsListBinding>(),
    ICT4DNewsRecyclerViewAdapter.OnICT4DNewsListClickListener {

    override fun getToolbarTitleResId(): Int = R.string.app_name

    override fun getLayoutId(): Int = R.layout.fragment_ictdnews_list

    override fun getViewModel(): Class<ICT4DNewsViewModel> = ICT4DNewsViewModel::class.java

    private val adapter: ICT4DNewsRecyclerViewAdapter = ICT4DNewsRecyclerViewAdapter(this)

    private var menu: Menu? = null

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

        model.isRefreshing.observe(this, Observer {
            binding.swiperefresh.isRefreshing = it ?: false
        })

        binding.swiperefresh.setOnRefreshListener {
            model.requestToLoadFeedsFromServers()
        }

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
                adapter.submitList(it)
            }
        })

        binding.quickScroll.setOnClickListener { binding.recyclerview.smoothScrollToPosition(0) }
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
                Timber.d("serach complete")
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
                model.requestToLoadFeedsFromServers()
                return true
            }

            // TODO("Move this call to More fragment when we have that fragment")
            R.id.menu_licences -> {
                activity?.let { startActivity(it.intentFor<OssLicensesMenuActivity>()) }
                OssLicensesMenuActivity.setActivityTitle(getString(R.string.open_source_licences_title))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onListItemClicked(item: News, view: View) {
        val action = ICT4DNewsFragmentDirections.ActionActionNewsToICT4DNewsDetailActivity(item)
        view.findNavController().navigate(action)
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ICT4DNewsFragment()
    }
}
