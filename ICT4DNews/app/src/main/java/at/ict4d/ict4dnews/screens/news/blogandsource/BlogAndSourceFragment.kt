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
import at.ict4d.ict4dnews.utils.BlogsAndSourceSubtitleUpdateMessage

class BlogAndSourceFragment : BaseFragment<BlogAndSourceViewModel, FragmentBlogAndSourcesBinding>() {
    override fun getToolbarTitleResId(): Int = R.string.ict4d_blogs_and_sources

    override fun getLayoutId(): Int = R.layout.fragment_blog_and_sources

    override fun getViewModel(): Class<BlogAndSourceViewModel> = BlogAndSourceViewModel::class.java

    private lateinit var blogAndSourceAdapter: BlogAndSourceRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        NavigationUI.setupWithNavController(binding.blogsAndSourceToolbar.toolbar, findNavController())

        compositeDisposable.add(rxEventBus.filteredObservable(BlogsAndSourceSubtitleUpdateMessage::class.java).subscribe {
            model.createSubtitleForContextualMenu(it.selectedBlogsSize, resources)
        })

        model.contextualMenuSubtitle.observe(this, Observer {
            binding.blogsAndSourceToolbar.toolbar.subtitle = it
        })

        blogAndSourceAdapter =
            BlogAndSourceRecyclerViewAdapter({ blog, checkBox ->
                handleOnClickOnBlogsAndSources(blog, checkBox)
            }, model.getContextualToolbarHandler())
        setUpNewsAndSourceRecyclerView()

        model.allBlogsList.observe(this, Observer {
            if (it != null && it.isNotEmpty()) {
                blogAndSourceAdapter.submitList(it)
                blogAndSourceAdapter.notifyDataSetChanged()
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
                model.updateBlogsActiveStatus(model.getContextualBlogsLiveData())
                activity?.invalidateOptionsMenu()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpNewsAndSourceRecyclerView() {
        binding.newsAndSourcesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.newsAndSourcesRecyclerView.setHasFixedSize(true)
        binding.newsAndSourcesRecyclerView.adapter = blogAndSourceAdapter
    }

    private fun handleOnClickOnBlogsAndSources(blog: Blog, checkBox: CheckBox) {
        blog.active = !blog.active
        checkBox.isChecked = blog.active
        model.updateBlogActiveStatus(blog)
    }
}