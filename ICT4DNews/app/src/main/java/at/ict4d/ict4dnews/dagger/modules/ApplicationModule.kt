package at.ict4d.ict4dnews.dagger.modules

import at.ict4d.ict4dnews.ICT4DNewsApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(private val ict4dNewsApplication: ICT4DNewsApplication) {

    @Provides
    @Singleton
    fun providesApplication(): ICT4DNewsApplication {
        return ict4dNewsApplication
    }
}