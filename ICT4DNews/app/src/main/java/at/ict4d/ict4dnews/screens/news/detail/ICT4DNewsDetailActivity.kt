package at.ict4d.ict4dnews.screens.news.detail

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.view.Menu
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
import at.ict4d.ict4dnews.extensions.browseCustomTab
import org.jetbrains.anko.share

const val KEY_NEWS_LIST_MODEL = "news_list_model"

class ICT4DNewsDetailActivity : BaseActivity<ICT4DNewsDetailViewModel, ActivityIct4DnewsDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_ict4_dnews_detail

    override fun getViewModel(): Class<ICT4DNewsDetailViewModel> = ICT4DNewsDetailViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fab.setOnClickListener { view ->
            val postModelList = intent.getParcelableExtra<News>(KEY_NEWS_LIST_MODEL)
            val postTitle= postModelList.title
            val postDate = postModelList.publishedDate?.extractDate()
            val postLink = postModelList.link
            val post = "$postTitle by ${author_name.text} - $postDate \n $postLink"
            share(post)
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

    override fun onBackPressed() {
        binding.fab.visible(false)
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ict4_dnews_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.fab.visible(false)
                supportFinishAfterTransition()
                return true
            }

            R.id.action_open -> {
                val url = intent.getParcelableExtra<News>(KEY_NEWS_LIST_MODEL).link
                browseCustomTab(url)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}