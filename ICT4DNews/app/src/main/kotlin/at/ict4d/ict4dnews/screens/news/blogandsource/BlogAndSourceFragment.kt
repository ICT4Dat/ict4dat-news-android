package at.ict4d.ict4dnews.screens.news.blogandsource

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentBlogAndSourcesBinding
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb

class BlogAndSourceFragment :
    BaseFragment<BlogAndSourceViewModel, FragmentBlogAndSourcesBinding>(
        R.layout.fragment_blog_and_sources,
        BlogAndSourceViewModel::class
    ) {

    private lateinit var blogAndSourceAdapter: BlogAndSourceRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        blogAndSourceAdapter = BlogAndSourceRecyclerViewAdapter { blog ->
            recordActionBreadcrumb("list click", this, mapOf("blog" to "$blog"))
            model.updateBlogActiveStatus(blog)
        }

        binding.newsAndSourcesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.newsAndSourcesRecyclerView.adapter = blogAndSourceAdapter

        model.isRefreshing.observe(this, Observer {
            binding.swiperefresh.isRefreshing = it ?: false
        })

        binding.swiperefresh.setOnRefreshListener {
            recordActionBreadcrumb("pull-to-refresh", this)
            model.refreshBlogs()
        }

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