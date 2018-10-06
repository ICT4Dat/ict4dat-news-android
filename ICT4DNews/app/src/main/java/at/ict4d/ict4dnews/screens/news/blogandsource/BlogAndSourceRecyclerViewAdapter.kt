package at.ict4d.ict4dnews.screens.news.blogandsource

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import at.ict4d.ict4dnews.databinding.BlogAndSourceItemBinding
import at.ict4d.ict4dnews.extensions.browseCustomTab
import at.ict4d.ict4dnews.models.Blog

class BlogAndSourceRecyclerViewAdapter(
    private val blogOnClickHandler: (Blog, CheckBox) -> Unit,
    private val contextualToolbarHandler: ContextualToolbarHandler
) :
    ListAdapter<Blog, BlogAndSourceRecyclerViewAdapter.ViewHolder>(BlogListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            BlogAndSourceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            blogOnClickHandler, contextualToolbarHandler
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setNewsItem(getItem(position))
    }

    class ViewHolder(
        private val binding: BlogAndSourceItemBinding,
        private val onClickHandler: (Blog, CheckBox) -> Unit,
        private val contextualToolbarHandler: ContextualToolbarHandler
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setNewsItem(blog: Blog) {
            binding.blog = blog
            binding.isBlogSelectedForContextualMenu = contextualToolbarHandler.isContextualModeEnable() && contextualToolbarHandler.isBlogAlreadySelected(blog)

            binding.root.setOnLongClickListener { contextualToolbarHandler.handleContextualRequest(blog,binding.root, true); true }

            binding.activeBlogCheckBox.setOnClickListener {
                if (!contextualToolbarHandler.isContextualModeEnable()) {
                    onClickHandler(blog, binding.activeBlogCheckBox)
                }
            }

            binding.root.setOnClickListener {
                if (!contextualToolbarHandler.isContextualModeEnable()) {
                    onClickHandler(blog, binding.activeBlogCheckBox)
                } else {
                    contextualToolbarHandler.handleContextualRequest(blog, binding.root)
                }
            }

            binding.activeBlogReadMoreButton.setOnClickListener { it.context.browseCustomTab(blog.url) }
        }
    }
}

class BlogListDiffCallback : DiffUtil.ItemCallback<Blog>() {

    override fun areItemsTheSame(oldItem: Blog, newItem: Blog): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: Blog, newItem: Blog): Boolean {
        return oldItem == newItem
    }
}