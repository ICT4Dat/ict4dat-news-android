package at.ict4d.ict4dnews.screens.news.blogandsource

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentBlogAndSourcesBinding
import at.ict4d.ict4dnews.extensions.handleApiResponse
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.screens.util.showOwnershipAlertDialog
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb
import org.koin.androidx.viewmodel.ext.android.viewModel

class BlogAndSourceFragment : BaseFragment<FragmentBlogAndSourcesBinding>(
    R.layout.fragment_blog_and_sources
) {

    private val model by viewModel<BlogAndSourceViewModel>()

    private lateinit var blogAndSourceAdapter: BlogAndSourceRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        blogAndSourceAdapter = BlogAndSourceRecyclerViewAdapter { blog ->
            recordActionBreadcrumb("list click", this, mapOf("blog" to "$blog"))
            model.updateBlogActiveStatus(blog)
        }

        binding.newsAndSourcesRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.newsAndSourcesRecyclerView.adapter = blogAndSourceAdapter

        binding.swiperefresh.setOnRefreshListener {
            recordActionBreadcrumb("pull-to-refresh", this)
            model.refreshBlogs()
        }

        model.allBlogsList.observe(viewLifecycleOwner) { resource ->

            handleApiResponse(resource, swipeRefreshLayout = binding.swiperefresh)

            if (resource.data != null && resource.data.isNotEmpty()) {
                blogAndSourceAdapter.submitList(resource.data)

                (activity as? AppCompatActivity)?.supportActionBar?.subtitle = getString(
                    R.string.contextual_selection,
                    resource.data.filter { blog -> blog.active }.size,
                    resource.data.size
                )
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    // Add menu items here
                    menuInflater.inflate(R.menu.blogs_and_sources_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem) = when (menuItem.itemId) {
                    R.id.info -> {
                        showOwnershipAlertDialog(requireContext())
                        true
                    }

                    else -> false
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED
        )
    }
}
