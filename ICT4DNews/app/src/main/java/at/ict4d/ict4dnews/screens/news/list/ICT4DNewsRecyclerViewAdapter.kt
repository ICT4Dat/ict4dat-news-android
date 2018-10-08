package at.ict4d.ict4dnews.screens.news.list

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import at.ict4d.ict4dnews.databinding.FragmentIctdnewsItemBinding
import at.ict4d.ict4dnews.models.News

class ICT4DNewsRecyclerViewAdapter(private val listener: OnICT4DNewsListClickListener) : ListAdapter<News, ICT4DNewsRecyclerViewAdapter.ViewHolder>(NewsListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentIctdnewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false), listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setNewsItem(getItem(position))
    }

    class ViewHolder(
        private val binding: FragmentIctdnewsItemBinding,
        private val onICT4DNewsListClickListener: OnICT4DNewsListClickListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun setNewsItem(newsItem: News) {
            binding.newsItem = newsItem
            binding.root.setOnClickListener { onICT4DNewsListClickListener.onListItemClicked(newsItem, binding.postImage) }
        }
    }

    interface OnICT4DNewsListClickListener {
        fun onListItemClicked(item: News, view: View)
    }
}

class NewsListDiffCallback : DiffUtil.ItemCallback<News>() {

    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.link == newItem.link
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem == newItem
    }
}
