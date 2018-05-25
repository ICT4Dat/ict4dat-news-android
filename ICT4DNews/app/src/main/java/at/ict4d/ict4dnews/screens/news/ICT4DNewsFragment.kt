package at.ict4d.ict4dnews.screens.news

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.*
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIctdnewsListBinding
import at.ict4d.ict4dnews.models.NewsListModel
import at.ict4d.ict4dnews.screens.base.BaseNavigationFragment
import com.jakewharton.rxbinding2.support.v7.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import timber.log.Timber
import java.util.concurrent.TimeUnit


/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [ICT4DNewsFragment.OnListFragmentInteractionListener] interface.
 */
class ICT4DNewsFragment : BaseNavigationFragment<ICT4DNewsViewModel, FragmentIctdnewsListBinding>(), ICT4DNewsRecyclerViewAdapter.OnICT4DNewsListClickListener {

    override fun getMenuItemId(): Int = R.id.navigation_news

    override fun getToolbarTitleResId(): Int = R.string.app_name

    override fun getLayoutId(): Int = R.layout.fragment_ictdnews_list

    override fun getViewModel(): Class<ICT4DNewsViewModel> = ICT4DNewsViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private var adapter: ICT4DNewsRecyclerViewAdapter = ICT4DNewsRecyclerViewAdapter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        binding.swiperefresh.setOnRefreshListener({
            // TODO: start refresh operation
            activity?.toast("refreshing....")
            binding.swiperefresh.isRefreshing = false
        })

        binding.recyclerview.layoutManager = LinearLayoutManager(context)
        binding.recyclerview.adapter = adapter

        model.newsListData.observe(this, Observer {
            it?.let {
                Timber.d("list in fragment: $it")
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
                    activity?.runOnUiThread({
                        activity?.toast("You will search for: $query")
                    })
                }, { e ->
                    Timber.e("*** error: $e")
                }, {
                    Timber.d("*** complete")
                }))
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {

            R.id.menu_refresh -> {

                binding.swiperefresh.isRefreshing = true

                // TODO: start refresh operation
                activity?.toast("refreshing....")
                binding.swiperefresh.isRefreshing = false

                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onListItemClicked(item: NewsListModel?) {
        activity?.toast("clicked on: ${item.toString()}")
    }

    companion object {

        @JvmStatic
        fun newInstance() =
                ICT4DNewsFragment()
    }
}
