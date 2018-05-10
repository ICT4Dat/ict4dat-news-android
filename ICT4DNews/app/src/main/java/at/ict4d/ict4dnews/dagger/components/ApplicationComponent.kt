package at.ict4d.ict4dnews.dagger.components

import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.dagger.modules.ApplicationModule
import at.ict4d.ict4dnews.lifecycle.RXErrorEventBusLifecycleObserver
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [], modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun application(): ICT4DNewsApplication

    fun inject(rxErrorEventBusLifecycleObserver: RXErrorEventBusLifecycleObserver)
}