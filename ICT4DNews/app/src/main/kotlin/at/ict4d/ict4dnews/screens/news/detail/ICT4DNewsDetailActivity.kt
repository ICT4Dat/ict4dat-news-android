package at.ict4d.ict4dnews.screens.news.detail

import androidx.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityIct4DnewsDetailBinding
import at.ict4d.ict4dnews.extensions.browseCustomTab
import at.ict4d.ict4dnews.extensions.extractDate
import at.ict4d.ict4dnews.extensions.loadFromURL
import at.ict4d.ict4dnews.extensions.visible
import at.ict4d.ict4dnews.screens.base.BaseActivity
import org.jetbrains.anko.share
import timber.log.Timber

class ICT4DNewsDetailActivity : BaseActivity<ICT4DNewsDetailViewModel, ActivityIct4DnewsDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_ict4_dnews_detail

    override fun getViewModel(): Class<ICT4DNewsDetailViewModel> = ICT4DNewsDetailViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // TODO: refactor to base Activity
        model.selectedNews = ICT4DNewsDetailActivityArgs.fromBundle(intent.extras).newsItem

        if (model.selectedNews == null) {
            Timber.e("Selected news must not be NULL")
            finish()
        }

        binding.appbarImage.loadFromURL(model.selectedNews?.mediaFeaturedURL ?: "")
        model.selectedNews?.blogID?.let { blogID ->
            model.getBlogBy(blogID).observe(this, Observer { blog ->
                blog?.let {
                    binding.toolbarLayout.title = it.name
                }
            })
        }

        val detailsFragment: ICT4DNewsDetailFragment? =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as ICT4DNewsDetailFragment?

        if (detailsFragment == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ICT4DNewsDetailFragment.newInstance(model.selectedNews)).commit()
        }

        binding.fab.setOnClickListener { _ ->

            model.selectedNews?.authorID?.let {
                model.authorDetails(it).observe(this, Observer { author ->
                    author?.name.let { authorName ->
                        share(
                            String.format(
                                getString(R.string.news_detail_share),
                                model.selectedNews?.title,
                                authorName,
                                model.selectedNews?.publishedDate?.extractDate(),
                                model.selectedNews?.link
                            )
                        )
                    }
                })
            }
        }
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
                browseCustomTab(model.selectedNews?.link ?: "")
            }
        }
        return super.onOptionsItemSelected(item)
    }
}