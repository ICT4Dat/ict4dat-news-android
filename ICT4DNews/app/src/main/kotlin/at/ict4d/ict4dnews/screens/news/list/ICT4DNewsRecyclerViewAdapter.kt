package at.ict4d.ict4dnews.screens.news.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import at.ict4d.ict4dnews.databinding.FragmentIctdnewsItemBinding
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.News
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import java.util.Locale
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class ICT4DNewsRecyclerViewAdapter(
    private val clickHandler: (Pair<News, Blog?>, view: View) -> Unit,
    var mostRecentNewsPublishDateTime: LocalDateTime = LocalDateTime.now().minusYears(10)
) : PagedListAdapter<Pair<News, Blog?>, ICT4DNewsRecyclerViewAdapter.ViewHolder>(NewsListDiffCallback()), FastScrollRecyclerView.SectionedAdapter {

    private val sectionNameDateFormatter = DateTimeFormatter.ofPattern("MMM yy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentIctdnewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsItem = getItem(position)
        if (newsItem == null) {
            holder.clear()
        } else {
            holder.setNewsItem(newsItem)
        }
    }

    inner class ViewHolder(private val binding: FragmentIctdnewsItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setNewsItem(pair: Pair<News, Blog?>) {
            binding.newsItem = pair.first
            binding.blog = pair.second
            binding.mostRecentNewsDateTime = mostRecentNewsPublishDateTime
            binding.root.setOnClickListener { clickHandler(pair, binding.postImage) }
        }

        fun clear() {
            binding.newsItem = null
            binding.blog = null
            binding.mostRecentNewsDateTime = null
            binding.root.setOnClickListener(null)
        }
    }

    override fun getSectionName(position: Int): String = getItem(position)?.first?.publishedDate?.format(sectionNameDateFormatter) ?: ""
}

class NewsListDiffCallback : DiffUtil.ItemCallback<Pair<News, Blog?>>() {

    override fun areItemsTheSame(oldItem: Pair<News, Blog?>, newItem: Pair<News, Blog?>): Boolean {
        return oldItem.first.link == newItem.first.link && oldItem.second?.feed_url == newItem.second?.feed_url
    }

    override fun areContentsTheSame(oldItem: Pair<News, Blog?>, newItem: Pair<News, Blog?>): Boolean {
        return oldItem.first == newItem.first && oldItem.second == newItem.second
    }
}
