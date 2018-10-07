package at.ict4d.ict4dnews.screens.news.detail

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.View
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4DnewsDetailBinding
import at.ict4d.ict4dnews.extensions.extractDate
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.screens.base.BaseFragment

const val ARG_NEWS_ITEM = "news_item"

class ICT4DNewsDetailFragment : BaseFragment<ICT4DNewsDetailViewModel, FragmentIct4DnewsDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_ict4_dnews_detail

    override fun getViewModel(): Class<ICT4DNewsDetailViewModel> = ICT4DNewsDetailViewModel::class.java

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        arguments?.getParcelable<News>(ARG_NEWS_ITEM)?.let {
            model.selectedNews = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (model.selectedNews != null && model.selectedNews?.authorID != null) {
            model.authorDetails(model.selectedNews?.authorID!!).observe(this, Observer {
                binding.authorName.text = it?.name ?: ""
            })
        }
        binding.blogTitle.text = model.selectedNews?.title
        binding.articleDate.text = model.selectedNews?.publishedDate?.extractDate()
        binding.webview.loadData(
            "<style>img{display: inline;height: auto;max-width: 100%;}</style>${model.selectedNews?.description}",
            "text/html; charset=utf-8",
            "UTF-8"
        )
    }

    companion object {
        fun newInstance(news: News?): ICT4DNewsDetailFragment = ICT4DNewsDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_NEWS_ITEM, news)
            }
        }
    }
}