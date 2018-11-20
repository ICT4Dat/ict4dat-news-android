package at.ict4d.ict4dnews.screens.news.detail

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4dNewsDetailBinding
import at.ict4d.ict4dnews.extensions.browseCustomTab
import at.ict4d.ict4dnews.extensions.extractDate
import at.ict4d.ict4dnews.extensions.loadFromURL
import at.ict4d.ict4dnews.screens.base.BaseFragment
import org.jetbrains.anko.share

class ICT4DNewsDetailFragment : BaseFragment<ICT4DNewsDetailViewModel, FragmentIct4dNewsDetailBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_ict4d_news_detail

    override fun getViewModel(): Class<ICT4DNewsDetailViewModel> = ICT4DNewsDetailViewModel::class.java

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        model.selectedNews = ICT4DNewsDetailFragmentArgs.fromBundle(arguments).newsItem
    }

    override fun onResume() {
        super.onResume()

        Handler().postDelayed({
            binding.fab.show()
        }, 700)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        NavigationUI.setupWithNavController(binding.toolbar, findNavController())
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        setHasOptionsMenu(true)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener { _ ->

            model.selectedNews?.authorID?.let {
                model.authorDetails(it).observe(this, Observer { author ->
                    author?.name.let { authorName ->
                        context?.share(
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

        binding.appbarImage.loadFromURL(model.selectedNews?.mediaFeaturedURL ?: "")

        model.selectedNews?.blogID?.let { blogID ->
            model.getBlogBy(blogID).observe(this, Observer { blog ->
                blog?.let { binding.toolbarLayout.title = it.name }
            })
        }

        if (model.selectedNews != null && model.selectedNews?.authorID != null) {
            model.authorDetails(model.selectedNews?.authorID!!).observe(this, Observer {
                binding.authorName.text = it?.name ?: ""
            })
        }
        binding.blogTitle.text = model.selectedNews?.title
        binding.articleDate.text = model.selectedNews?.publishedDate?.extractDate()

        model.selectedNews?.description?.let { html ->

            // enables JS for youtube videos
            binding.webview.settings.javaScriptEnabled = true

            binding.webview.loadData(
                "$html<style>img{display: inline;height: auto;max-width: 100%;}</style>",
                "text/html; charset=utf-8",
                "UTF-8"
            )
        }

        binding.webview.webViewClient = object : WebViewClient() {

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                return if (url.startsWith("www", true) || url.startsWith("http", true)) {
                    activity?.browseCustomTab(url)
                    true
                } else {
                    super.shouldOverrideUrlLoading(view, request)
                }
            }

            @Suppress("DEPRECATION", "OverridingDeprecatedMember")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return if (url != null && (url.startsWith("www", true) || url.startsWith("http", true))) {
                    activity?.browseCustomTab(url)
                    true
                } else {
                    super.shouldOverrideUrlLoading(view, url)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_ict4_dnews_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_open -> activity?.browseCustomTab(model.selectedNews?.link ?: "")
        }
        return super.onOptionsItemSelected(item)
    }
}