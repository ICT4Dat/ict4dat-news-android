package at.ict4d.ict4dnews.screens.news.detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.view.View
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4DnewsDetailBinding
import at.ict4d.ict4dnews.extensions.extractDate
import at.ict4d.ict4dnews.screens.base.BaseFragment

class ICT4DNewsDetailFragment : BaseFragment<ICT4DNewsDetailViewModel, FragmentIct4DnewsDetailBinding>() {

    override fun getToolbarTitleResId(): Int = R.string.title_activity_ict4_dnews_detail

    override fun getLayoutId(): Int = R.layout.fragment_ict4_dnews_detail

    override fun getViewModel(): Class<ICT4DNewsDetailViewModel> = ICT4DNewsDetailViewModel::class.java

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.postText.text = model.selectedNews?.description
        if (model.selectedNews != null && model.selectedNews?.authorID != null) {
            model.authorDetails(model.selectedNews?.authorID!!).observe(this, Observer {
                binding.authorName.text = it?.name ?: ""
            })
        }
        binding.blogTitle.text = model.selectedNews?.title
        binding.postText.text = model.selectedNews?.description
        binding.articleDate.text = model.selectedNews?.publishedDate?.extractDate()
    }

    companion object {
        fun newInstance(): ICT4DNewsDetailFragment = ICT4DNewsDetailFragment()
    }
}