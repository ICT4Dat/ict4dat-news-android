package at.ict4d.ict4dnews.dagger.scopes

import javax.inject.Scope

/**
 * A custom scoping annotation that specifies that the lifespan of a dependency be the same as that
 * of an Activity.
 *
 * This is used to annotate dependencies that behave like a singleton within the lifespan of an
 * Activity, Fragment, and child Fragments instead of the entire Application.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity