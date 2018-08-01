package at.ict4d.ict4dnews.screens.news.detail

import android.arch.lifecycle.Observer
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
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
import android.support.customtabs.CustomTabsIntent

const val KEY_NEWS_LIST_MODEL = "news_list_model"

class ICT4DNewsDetailActivity : BaseActivity<ICT4DNewsDetailViewModel, ActivityIct4DnewsDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_ict4_dnews_detail

    override fun getViewModel(): Class<ICT4DNewsDetailViewModel> = ICT4DNewsDetailViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fab.setOnClickListener { view ->
            val linkShare =intent.getParcelableExtra<News>(KEY_NEWS_LIST_MODEL)
            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.putExtra(Intent.EXTRA_TEXT, linkShare.link)
            sharingIntent.setType("text/plain")
            this.startActivity(Intent.createChooser(sharingIntent, resources.getText(R.string.send_to)))
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
                ChromeTabload()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun ChromeTabload() {
        val newsLink = intent.getParcelableExtra<News>(KEY_NEWS_LIST_MODEL)
        val url = newsLink.link
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(url))
    }
}