package at.ict4d.ict4dnews.screens.news.blogandsource

import androidx.lifecycle.Observer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.View
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentBlogAndSourcesBinding
import at.ict4d.ict4dnews.screens.base.BaseFragment

class BlogAndSourceFragment : BaseFragment<BlogAndSourceViewModel, FragmentBlogAndSourcesBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_blog_and_sources

    override fun getViewModel(): Class<BlogAndSourceViewModel> = BlogAndSourceViewModel::class.java

    private lateinit var blogAndSourceAdapter: BlogAndSourceRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        blogAndSourceAdapter = BlogAndSourceRecyclerViewAdapter { blog ->
            model.updateBlogActiveStatus(blog)
        }

        binding.newsAndSourcesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.newsAndSourcesRecyclerView.adapter = blogAndSourceAdapter

        model.isRefreshing.observe(this, Observer {
            binding.swiperefresh.isRefreshing = it ?: false
        })

        binding.swiperefresh.setOnRefreshListener { model.refreshBlogs() }

        model.allBlogsList.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                blogAndSourceAdapter.submitList(it)
                (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(
                    R.string.contextual_selection,
                    it.filter { blog -> blog.active == true }.size,
                    it.size
                )
            }
        })
    }
}