package at.ict4d.ict4dnews.dagger.components

import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.dagger.modules.ApiServiceModule
import at.ict4d.ict4dnews.dagger.modules.ApplicationModule
import at.ict4d.ict4dnews.dagger.modules.RoomModule
import at.ict4d.ict4dnews.lifecycle.RXErrorEventBusLifecycleObserver
import at.ict4d.ict4dnews.persistence.PersistenceManager
import at.ict4d.ict4dnews.persistence.database.Converters
import at.ict4d.ict4dnews.screens.MainNavigationActivity
import at.ict4d.ict4dnews.server.Server
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(dependencies = [], modules = [ApplicationModule::class, ApiServiceModule::class, RoomModule::class])
interface ApplicationComponent {

    fun application(): ICT4DNewsApplication

    fun inject(rxErrorEventBusLifecycleObserver: RXErrorEventBusLifecycleObserver)

    fun inject(server: Server)

    fun inject(converters: Converters)

    fun inject(persistenceManager: PersistenceManager)

    fun inject(mainNavigationActivity: MainNavigationActivity)

}