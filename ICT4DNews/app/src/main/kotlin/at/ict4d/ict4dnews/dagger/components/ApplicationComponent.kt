package at.ict4d.ict4dnews.dagger.components

import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.background.UpdateNewsWorker
import at.ict4d.ict4dnews.dagger.modules.ApiServiceModule
import at.ict4d.ict4dnews.dagger.modules.ApplicationModule
import at.ict4d.ict4dnews.dagger.modules.HelperModule
import at.ict4d.ict4dnews.dagger.modules.RoomModule
import at.ict4d.ict4dnews.dagger.modules.ViewModelFactoryModule
import at.ict4d.ict4dnews.persistence.database.Converters
import at.ict4d.ict4dnews.screens.more.settings.SettingsFragment
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class, RoomModule::class, ApiServiceModule::class, HelperModule::class, ViewModelFactoryModule::class])
interface ApplicationComponent : AndroidInjector<ICT4DNewsApplication> {

    @Component.Factory
    abstract class Factory : AndroidInjector.Factory<ICT4DNewsApplication>

    fun inject(converters: Converters)

    fun inject(settingsFragment: SettingsFragment)

    fun inject(updateNewsWorker: UpdateNewsWorker)
}