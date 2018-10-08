package at.ict4d.ict4dnews.screens.news.blogandsource

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import at.ict4d.ict4dnews.databinding.BlogAndSourceItemBinding
import at.ict4d.ict4dnews.extensions.browseCustomTab
import at.ict4d.ict4dnews.models.Blog

class BlogAndSourceRecyclerViewAdapter(private val clickHandler: (Blog) -> Unit) : ListAdapter<Blog, BlogAndSourceRecyclerViewAdapter.ViewHolder>(BlogListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(BlogAndSourceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItem(getItem(position))
    }

    inner class ViewHolder(private val binding: BlogAndSourceItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setItem(blog: Blog) {
            binding.blog = blog

            binding.root.setOnClickListener {
                handleClick(blog)
            }

            binding.activeBlogCheckBox.setOnClickListener {
                handleClick(blog)
            }

            binding.activeBlogReadMoreButton.setOnClickListener {
                it.context.browseCustomTab(blog.url)
            }
        }

        private fun handleClick(blog: Blog) {
            blog.active = !blog.active
            binding.activeBlogCheckBox.isChecked = blog.active
            clickHandler(blog)
        }
    }
}

class BlogListDiffCallback : DiffUtil.ItemCallback<Blog>() {

    override fun areItemsTheSame(oldItem: Blog, newItem: Blog): Boolean {
        return oldItem.feed_url == newItem.feed_url
    }

    override fun areContentsTheSame(oldItem: Blog, newItem: Blog): Boolean {
        return oldItem == newItem
    }
}