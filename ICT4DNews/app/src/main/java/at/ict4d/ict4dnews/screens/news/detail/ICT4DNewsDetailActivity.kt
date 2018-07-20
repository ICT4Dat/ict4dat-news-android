package at.ict4d.ict4dnews.screens.news.detail

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.models.NewsModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_ict4_dnews_detail.*
import kotlinx.android.synthetic.main.content_ict4_dnews_detail.*
import timber.log.Timber

const val KEY_NEWS_LIST_MODEL = "news_list_model"

class ICT4DNewsDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ict4_dnews_detail)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val newsListModel = intent.getParcelableExtra<NewsModel>(KEY_NEWS_LIST_MODEL)
        Timber.d("Model: ${newsListModel.mediaFeaturedURL}")

        title = newsListModel.title
        post_text.text = newsListModel.description

        Glide.with(this).load(newsListModel.mediaFeaturedURL).into(appbar_image)
    }
}
