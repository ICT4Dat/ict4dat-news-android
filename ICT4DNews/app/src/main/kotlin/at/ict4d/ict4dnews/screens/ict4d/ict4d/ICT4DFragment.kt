package at.ict4d.ict4dnews.screens.ict4d.ict4d

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentIct4dBinding
import at.ict4d.ict4dnews.extensions.browseCustomTab
import at.ict4d.ict4dnews.screens.base.BaseFragment

class ICT4DFragment : BaseFragment<FragmentIct4dBinding>(
    R.layout.fragment_ict4d,
    hasToolbar = false
) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        binding.fragment = this

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_ict4d, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem) = when (menuItem.itemId) {
                R.id.menu_ict4d_share -> {

                    startActivity(
                        Intent.createChooser(
                            Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    getString(
                                        R.string.share_ict4d,
                                        getString(R.string.url_ict4d_wikipedia)
                                    )
                                )
                                type = "text/plain"
                            },
                            getString(R.string.share)
                        )
                    )

                    true
                }

                else -> false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    fun ict4dWikipedia() {
        context?.browseCustomTab(getString(R.string.url_ict4d_wikipedia))
    }

    companion object {

        @JvmStatic
        fun newInstance() = ICT4DFragment()
    }
}
