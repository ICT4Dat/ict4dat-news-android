package at.ict4d.ict4dnews.screens.news.detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityIct4DnewsDetailBinding
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.screens.base.BaseActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_ict4_dnews_detail.*
import timber.log.Timber

const val KEY_NEWS_LIST_MODEL = "news_list_model"

class ICT4DNewsDetailActivity : BaseActivity<ICT4DNewsDetailViewModel, ActivityIct4DnewsDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_ict4_dnews_detail

    override fun getViewModel(): Class<ICT4DNewsDetailViewModel> = ICT4DNewsDetailViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // TODO: refactor to base Activity
        model.selectedNews = intent.getParcelableExtra<News>(KEY_NEWS_LIST_MODEL)
        Timber.d("Model: ${model.selectedNews?.mediaFeaturedURL}")
        Glide.with(this).load(model.selectedNews?.mediaFeaturedURL).into(appbar_image)
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        var detailsFragment: ICT4DNewsDetailFragment? =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as ICT4DNewsDetailFragment?

        if (detailsFragment == null) {
            detailsFragment = ICT4DNewsDetailFragment.newInstance()
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, detailsFragment).commit()
        }

        if (model.selectedNews == null) {
            Timber.e("Selected news must not be NULL")
            finish()
        }
    }
}
