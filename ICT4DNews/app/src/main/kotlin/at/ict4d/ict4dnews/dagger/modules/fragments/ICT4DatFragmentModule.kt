package at.ict4d.ict4dnews.dagger.modules.fragments

import android.support.v4.app.Fragment
import at.ict4d.ict4dnews.dagger.scopes.PerFragment
import at.ict4d.ict4dnews.screens.ict4dat.ICT4DatFragment
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class ICT4DatFragmentModule {

    @Binds
    @PerFragment
    abstract fun fragment(fragment: ICT4DatFragment): Fragment
}