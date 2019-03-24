package at.ict4d.ict4dnews.screens.more.settings

import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentSettingsWithToolbarBinding
import at.ict4d.ict4dnews.screens.base.BaseFragment

class SettingsFragmentWithToolbar :
    BaseFragment<SettingsWithToolbarViewModel, FragmentSettingsWithToolbarBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_settings_with_toolbar

    override fun getViewModel(): Class<SettingsWithToolbarViewModel> =
        SettingsWithToolbarViewModel::class.java
}