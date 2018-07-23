package at.ict4d.ict4dnews.dagger.components

import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.dagger.modules.*
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, ApplicationModule::class, RoomModule::class, ApiServiceModule::class, HelperModule::class, ViewModelFactoryModule::class])
interface ApplicationComponent : AndroidInjector<ICT4DNewsApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ICT4DNewsApplication>()
}