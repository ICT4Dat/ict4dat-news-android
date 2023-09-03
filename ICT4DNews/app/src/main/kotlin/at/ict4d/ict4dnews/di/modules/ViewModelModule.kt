package at.ict4d.ict4dnews.di.modules

import at.ict4d.ict4dnews.screens.MainNavigationViewModel
import at.ict4d.ict4dnews.screens.more.MoreViewModel
import at.ict4d.ict4dnews.screens.more.settings.SettingsWithToolbarViewModel
import at.ict4d.ict4dnews.screens.news.blogandsource.BlogAndSourceViewModel
import at.ict4d.ict4dnews.screens.news.list.ICT4DNewsViewModel
import at.ict4d.ict4dnews.screens.welcome.setup.WelcomeSetupViewModel
import at.ict4d.ict4dnews.screens.welcome.summary.WelcomeSummaryViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { ICT4DNewsViewModel(get(), get(), get()) }

    viewModel { MoreViewModel() }

    viewModel { MainNavigationViewModel(get(), get()) }

    viewModel { BlogAndSourceViewModel(get()) }

    viewModel { WelcomeSetupViewModel(get()) }

    viewModel { SettingsWithToolbarViewModel() }

    viewModel { WelcomeSummaryViewModel(get(), get()) }
}
