package at.ict4d.ict4dnews.screens.news.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4dNewsDetailBinding
import at.ict4d.ict4dnews.extensions.browseCustomTab
import at.ict4d.ict4dnews.extensions.extractDate
import at.ict4d.ict4dnews.screens.base.BaseFragment
import at.ict4d.ict4dnews.utils.recordActionBreadcrumb
import org.koin.androidx.viewmodel.ext.android.viewModel

class ICT4DNewsDetailFragment :
    BaseFragment<FragmentIct4dNewsDetailBinding>(
        R.layout.fragment_ict4d_news_detail
    ) {

    private val model by viewModel<ICT4DNewsDetailViewModel>()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        model.setUp(ICT4DNewsDetailFragmentArgs.fromBundle(requireArguments()).newsItem)
    }

    override fun onResume() {
        super.onResume()

        Handler(Looper.getMainLooper()).postDelayed({
            binding.fab.show()
        }, 700)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            recordActionBreadcrumb("fab", this)

            model.author.value?.name.let { authorName ->

                startActivity(
                    Intent.createChooser(
                        Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                getString(
                                    R.string.news_detail_share,
                                    model.getNews()?.title,
                                    authorName,
                                    model.getNews()?.publishedDate?.extractDate(),
                                    model.getNews()?.link
                                )
                            )
                            type = "text/plain"
                        },
                        getString(R.string.share)
                    )
                )
            }
        }

        binding.news = model.getNews()

        model.author.observe(viewLifecycleOwner) {
            binding.author = it
        }

        model.blog.observe(viewLifecycleOwner) {
            binding.blog = it
        }

        model.getNews()?.description?.let { html ->

            // enables JS for youtube videos
            binding.webview.settings.javaScriptEnabled = true

            binding.webview.loadData(
                "$html<style>img{display: inline;height: auto;max-width: 100%;}</style>",
                "text/html; charset=utf-8",
                "UTF-8"
            )
        }

        binding.webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url.toString()
                return if (url.startsWith("www", true) || url.startsWith("http", true)) {
                    activity?.browseCustomTab(url)
                    true
                } else {
                    super.shouldOverrideUrlLoading(view, request)
                }
            }

            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return if (url != null && (url.startsWith("www", true) || url.startsWith(
                        "http",
                        true
                    ))
                ) {
                    activity?.browseCustomTab(url)
                    true
                } else {
                    super.shouldOverrideUrlLoading(view, url)
                }
            }
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_ict4_dnews_detail, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem) = when (menuItem.itemId) {
                R.id.action_open -> {
                    activity?.browseCustomTab(model.getNews()?.link ?: "")
                    true
                }

                else -> false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }
}
