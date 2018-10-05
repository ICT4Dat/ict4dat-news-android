package at.ict4d.ict4dnews.screens.news.blogandsource

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
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
        setHasOptionsMenu(true)
        NavigationUI.setupWithNavController(binding.blogsAndSourceToolbar.toolbar, findNavController())
        blogAndSourceAdapter =
            BlogAndSourceRecyclerViewAdapter({ blog, checkBox -> handleOnClickOnBlogsAndSources(blog, checkBox) },
                { handleContextualBlogsAndSources(it) })
        setUpNewsAndSourceRecyclerView()

        model.allBlogsList.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                blogAndSourceAdapter.submitList(it)
                blogAndSourceAdapter.notifyDataSetChanged()
            }
        })

        model.getContextualBlogsLiveData().observe(this, Observer { it ->
            it?.let {
                binding.blogsAndSourceToolbar.toolbar.subtitle = if (it.size > 0) {
                    String.format(getString(R.string.contextual_selection), it.size, if (it.size == 1) "" else "s")
                } else {
                    null
                }
            }
        })

        model.shouldShowCheckSelected().observe(this, Observer { activity?.invalidateOptionsMenu() })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.apply { inflate(R.menu.blogs_and_source_menu, menu) }
        menu?.apply {
            val shouldShowCheckIcon = model.shouldShowCheckSelected().value
            val menuItem = findItem(R.id.actionCheckBlogsAndSources)
            if (shouldShowCheckIcon != null && model.isContextualRequestEnable()) {
                menuItem.isVisible = true
                context?.let {
                    menuItem.icon = if (shouldShowCheckIcon) {
                        ContextCompat.getDrawable(it, R.drawable.ic_check_box_black_24dp)
                    } else {
                        ContextCompat.getDrawable(it, R.drawable.ic_check_box_outline_blank_black_24dp)
                    }
                }
            } else {
                menuItem.isVisible = false
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.actionCheckBlogsAndSources -> {
                val selectedList = model.getContextualBlogsLiveData().value
                selectedList?.let {
                    model.updateBlogsActiveStatus(it)
                    activity?.invalidateOptionsMenu()
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpNewsAndSourceRecyclerView() {
        binding.newsAndSourcesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.newsAndSourcesRecyclerView.adapter = blogAndSourceAdapter
    }

    private fun handleOnClickOnBlogsAndSources(blog: Blog, checkBox: CheckBox) {
        if (!model.isContextualRequestEnable()) {
            blog.active = !blog.active
            checkBox.isChecked = blog.active
            model.updateBlogActiveStatus(blog)
        } else {
            model.handleContextualRequest(blog)
        }
    }

    private fun handleContextualBlogsAndSources(blog: Blog) {
        model.handleContextualRequest(blog, true)
    }
}