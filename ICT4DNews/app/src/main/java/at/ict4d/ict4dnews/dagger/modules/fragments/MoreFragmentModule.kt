package at.ict4d.ict4dnews.dagger.modules.fragments

import android.support.v4.app.Fragment
import at.ict4d.ict4dnews.dagger.scopes.PerFragment
import at.ict4d.ict4dnews.screens.more.MoreFragment
import dagger.Binds
import dagger.Module

@Suppress("unused")
@Module
abstract class MoreFragmentModule {

    @Binds
    @PerFragment
    abstract fun fragment(fragment: MoreFragment): Fragment
}