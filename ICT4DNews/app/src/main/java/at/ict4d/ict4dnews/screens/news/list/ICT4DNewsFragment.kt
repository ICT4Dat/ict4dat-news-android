package at.ict4d.ict4dnews.screens.news.list

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIctdnewsListBinding
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.screens.base.BaseNavigationFragment
import at.ict4d.ict4dnews.screens.news.detail.ICT4DNewsDetailActivity
import at.ict4d.ict4dnews.screens.news.detail.KEY_NEWS_LIST_MODEL
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import timber.log.Timber
import java.util.concurrent.TimeUnit

class ICT4DNewsFragment : BaseNavigationFragment<ICT4DNewsViewModel, FragmentIctdnewsListBinding>(),
    ICT4DNewsRecyclerViewAdapter.OnICT4DNewsListClickListener {

    override fun getMenuItemId(): Int = R.id.navigation_news

    override fun getToolbarTitleResId(): Int = R.string.app_name

    override fun getLayoutId(): Int = R.layout.fragment_ictdnews_list

    override fun getViewModel(): Class<ICT4DNewsViewModel> = ICT4DNewsViewModel::class.java

    private val adapter: ICT4DNewsRecyclerViewAdapter = ICT4DNewsRecyclerViewAdapter(this)

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
            model.requestToLoadJsonFeed()
        }

        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = adapter

        model.newsList.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                Timber.d("list in fragment: ${it.size}")
                adapter.submitList(it)
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
            .filter { query: String -> !query.isEmpty() }
            .observeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ query ->
                Timber.d("*** query: $query")
                activity?.runOnUiThread {
                    activity?.toast("You will search for: $query")
                }
            }, { e ->
                Timber.e("*** error: $e")
            }, {
                Timber.d("*** complete")
            })
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {

            R.id.menu_refresh -> {
                model.requestToLoadJsonFeed()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onListItemClicked(item: News, view: View) {
        activity?.let {
            val intent = Intent(it, ICT4DNewsDetailActivity::class.java)
            intent.putExtra(KEY_NEWS_LIST_MODEL, item)
            val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(it, view, "post_image")
            startActivity(intent, optionsCompat.toBundle())
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            ICT4DNewsFragment()
    }
}
