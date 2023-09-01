package at.ict4d.ict4dnews.di.modules

import at.ict4d.ict4dnews.screens.MainNavigationViewModel
import at.ict4d.ict4dnews.screens.more.MoreViewModel
import at.ict4d.ict4dnews.screens.more.settings.SettingsWithToolbarViewModel
import at.ict4d.ict4dnews.screens.news.blogandsource.BlogAndSourceViewModel
import at.ict4d.ict4dnews.screens.news.detail.ICT4DNewsDetailViewModel
import at.ict4d.ict4dnews.screens.news.list.ICT4DNewsViewModel
import at.ict4d.ict4dnews.screens.splashscreen.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { ICT4DNewsViewModel(get(), get(), get()) }

    viewModel { MoreViewModel() }

    viewModel { ICT4DNewsDetailViewModel(get(), get()) }

    viewModel { MainNavigationViewModel(get(), get()) }

    viewModel { BlogAndSourceViewModel(get()) }

    viewModel { SplashViewModel(get()) }

    viewModel { SettingsWithToolbarViewModel() }
}
