package at.ict4d.ict4dnews.screens.news.detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.design.widget.Snackbar
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityIct4DnewsDetailBinding
import at.ict4d.ict4dnews.models.NewsModel
import at.ict4d.ict4dnews.screens.base.BaseActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_ict4_dnews_detail.*
import kotlinx.android.synthetic.main.content_ict4_dnews_detail.*
import timber.log.Timber

const val KEY_NEWS_LIST_MODEL = "news_list_model"

class ICT4DNewsDetailActivity : BaseActivity<ICT4DNewsDetailViewModel, ActivityIct4DnewsDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_ict4_dnews_detail

    override fun getViewModel(): Class<ICT4DNewsDetailViewModel> = ICT4DNewsDetailViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // TODO: refactor to base Activity
        val newsModelList = intent.getParcelableExtra<NewsModel>(KEY_NEWS_LIST_MODEL)
        Timber.d("Model: ${newsModelList.mediaFeaturedURL}")
        if (newsModelList != null) {
            model.loadAuthorDetails(newsModelList.authorID).observe(this, Observer {
                if (it != null) {
                    author_name.text = it.name
                }
            })
        }
//        title = getString(R.string.nav_ict4dat)
        blog_title.text = newsModelList.title
        post_text.text = newsModelList.description

        Glide.with(this).load(newsModelList.mediaFeaturedURL).into(appbar_image)
    }
}
