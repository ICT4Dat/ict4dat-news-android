package at.ict4d.ict4dnews.dagger.modules

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import at.ict4d.ict4dnews.screens.MainNavigationViewModel
import at.ict4d.ict4dnews.screens.ict4dat.ICT4DatViewModel
import at.ict4d.ict4dnews.screens.more.MoreViewModel
import at.ict4d.ict4dnews.screens.news.detail.ICT4DNewsDetailViewModel
import at.ict4d.ict4dnews.screens.news.list.ICT4DNewsViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
abstract class ViewModelFactoryModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: DaggerViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(ICT4DNewsViewModel::class)
    abstract fun bindsICT4DNewsViewModel(viewModel: ICT4DNewsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MoreViewModel::class)
    abstract fun bindsMoreViewModel(viewModel: MoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ICT4DatViewModel::class)
    abstract fun bindsICT4DatViewModel(viewModel: ICT4DatViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ICT4DNewsDetailViewModel::class)
    abstract fun bindsICT4DNewsDetailViewModel(viewModel: ICT4DNewsDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainNavigationViewModel::class)
    abstract fun bindsMainNavigationViewModelViewModel(viewModel: MainNavigationViewModel): ViewModel
}

@Suppress("UNCHECKED_CAST")
@Singleton
class DaggerViewModelFactory
@Inject constructor(
    private val creators: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val creator = creators[modelClass]
                ?: creators.asIterable().firstOrNull { modelClass.isAssignableFrom(it.key) }?.value
                ?: throw IllegalArgumentException("unknown model class " + modelClass)

        return try {
            creator.get() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)