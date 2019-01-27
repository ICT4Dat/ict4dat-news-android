package at.ict4d.ict4dnews.screens.news.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import at.ict4d.ict4dnews.databinding.FragmentIctdnewsItemBinding
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.News

class ICT4DNewsRecyclerViewAdapter(private val clickHandler: (News, View) -> Unit) :
    ListAdapter<Pair<News, Blog>, ICT4DNewsRecyclerViewAdapter.ViewHolder>(NewsListDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentIctdnewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setNewsItem(getItem(position))
    }

    inner class ViewHolder(private val binding: FragmentIctdnewsItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun setNewsItem(pair: Pair<News, Blog>) {
            binding.newsItem = pair.first
            binding.blog = pair.second

            binding.root.setOnClickListener {
                val readNews = pair.first.copy(readState = true)
                binding.newsItem = readNews
                clickHandler(readNews, binding.postImage)
            }
        }
    }
}

class NewsListDiffCallback : DiffUtil.ItemCallback<Pair<News, Blog>>() {

    override fun areItemsTheSame(oldItem: Pair<News, Blog>, newItem: Pair<News, Blog>): Boolean {
        return oldItem.first.link == newItem.first.link && oldItem.second.feed_url == newItem.second.feed_url
    }

    override fun areContentsTheSame(oldItem: Pair<News, Blog>, newItem: Pair<News, Blog>): Boolean {
        return oldItem.first == newItem.first && oldItem.second == newItem.second
    }
}
