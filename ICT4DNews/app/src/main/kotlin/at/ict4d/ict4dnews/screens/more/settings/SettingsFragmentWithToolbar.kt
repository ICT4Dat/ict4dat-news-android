package at.ict4d.ict4dnews.screens.more.settings

import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.databinding.FragmentSettingsWithToolbarBinding
import at.ict4d.ict4dnews.screens.base.BaseFragment

class SettingsFragmentWithToolbar :
    BaseFragment<SettingsWithToolbarViewModel, FragmentSettingsWithToolbarBinding>(
        R.layout.fragment_settings_with_toolbar,
        SettingsWithToolbarViewModel::class
    )