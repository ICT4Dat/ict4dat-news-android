package at.ict4d.ict4dnews.screens.news.blogandsource

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.CheckBox
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentBlogAndSourcesBinding
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.screens.base.BaseFragment

class BlogAndSourceFragment : BaseFragment<BlogAndSourceViewModel, FragmentBlogAndSourcesBinding>() {
    override fun getToolbarTitleResId(): Int = R.string.ict4d_blogs_and_sources

    override fun getLayoutId(): Int = R.layout.fragment_blog_and_sources

    override fun getViewModel(): Class<BlogAndSourceViewModel> = BlogAndSourceViewModel::class.java

    private lateinit var blogAndSourceAdapter: BlogAndSourceRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        blogAndSourceAdapter =
            BlogAndSourceRecyclerViewAdapter { blog, checkBox -> handleOnClickOnBlogsAndSources(blog, checkBox) }
        setUpNewsAndSourceRecyclerView()

        model.allBlogsList.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                blogAndSourceAdapter.submitList(it)
            }
        })
    }

    private fun setUpNewsAndSourceRecyclerView() {
        binding.newsAndSourcesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.newsAndSourcesRecyclerView.adapter = blogAndSourceAdapter
    }

    private fun handleOnClickOnBlogsAndSources(blog: Blog, checkBox: CheckBox) {
        blog.active = !blog.active
        checkBox.isChecked = blog.active
        model.updateBlogActiveStatus(blog)
    }
}