package at.ict4d.ict4dnews.screens.news.list

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.models.NewsModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_ictdnews_item.view.*

class ICT4DNewsRecyclerViewAdapter(private val mListener: OnICT4DNewsListClickListener?) : ListAdapter<NewsModel, ICT4DNewsRecyclerViewAdapter.ViewHolder>(NewsListDiffCallback()) {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as NewsModel
            mListener?.onListItemClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_ictdnews_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.headlineView.text = item.title

        if (item.mediaFeaturedURL == null) {
            holder.imageView.setImageDrawable(null)
        } else {
            Glide.with(holder.imageView.context).load(item.mediaFeaturedURL).apply(RequestOptions.circleCropTransform()).into(holder.imageView)
            holder.imageView.contentDescription = item.title
        }

        with(holder.view) {
            tag = item
            setOnClickListener(mOnClickListener)
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val headlineView: TextView = view.headline
        val imageView: ImageView = view.postImage
    }

    interface OnICT4DNewsListClickListener {
        fun onListItemClicked(item: NewsModel?)
    }
}

class NewsListDiffCallback : DiffUtil.ItemCallback<NewsModel>() {
    override fun areItemsTheSame(oldItem: NewsModel?, newItem: NewsModel?): Boolean {
        return oldItem?.link == newItem?.link
    }

    override fun areContentsTheSame(oldItem: NewsModel?, newItem: NewsModel?): Boolean {
        return oldItem == newItem
    }
}
