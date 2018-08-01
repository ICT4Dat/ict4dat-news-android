package at.ict4d.ict4dnews.screens.news.detail

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.view.MenuItem
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.ActivityIct4DnewsDetailBinding
import at.ict4d.ict4dnews.extensions.loadImage
import at.ict4d.ict4dnews.extensions.visible
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.screens.base.BaseActivity
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
        binding.appbarImage.loadImage(model.selectedNews?.mediaFeaturedURL)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
//        title = getString(R.string.nav_ict4dat)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                binding.fab.visible(false)
                supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
