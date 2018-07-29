package at.ict4d.ict4dnews.screens.news.detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.view.MenuItem
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityIct4DnewsDetailBinding
import at.ict4d.ict4dnews.extensions.extractDate
import at.ict4d.ict4dnews.extensions.loadImage
import at.ict4d.ict4dnews.extensions.visible
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.screens.base.BaseActivity

import kotlinx.android.synthetic.main.activity_ict4_dnews_detail.*
import kotlinx.android.synthetic.main.content_ict4_dnews_detail.*

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
        val newsModelList = intent.getParcelableExtra<News>(KEY_NEWS_LIST_MODEL)

        if (newsModelList != null) {
            model.authorDetails(newsModelList.authorID).observe(this, Observer {
                author_name.text = it?.name ?: ""
            })
        }

        blog_title.text = newsModelList.title
        post_text.text = newsModelList.description
        article_date.text = newsModelList.publishedDate?.extractDate()

        binding.appbarImage.loadImage(newsModelList.mediaFeaturedURL)
    }

    override fun onResume() {
        super.onResume()

        Handler().postDelayed({
            binding.fab.show()
        }, 700)
    }

    override fun onPause() {
        binding.fab.visible(false)
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
