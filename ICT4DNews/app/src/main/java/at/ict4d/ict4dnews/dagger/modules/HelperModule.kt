package at.ict4d.ict4dnews.dagger.modules

import android.arch.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class HelperModule {

    @Provides
    fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
}