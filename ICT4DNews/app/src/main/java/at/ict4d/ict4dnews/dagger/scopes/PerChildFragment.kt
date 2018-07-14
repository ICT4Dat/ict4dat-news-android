package at.ict4d.ict4dnews.dagger.scopes

import javax.inject.Scope

/**
 * A custom scoping annotation that specifies that the lifespan of a dependency be the same as that
 * of a child Fragment (a fragment inside a fragment that is added using
 * Fragment.getChildFragmentManager().
 *
 * This is used to annotate dependencies that behave like a singleton within the lifespan of a child
 * Fragment instead of the entire Application, Activity, or parent Fragment.
 *
 * Note that this does not support a child fragment within a child fragment as conflicting scopes
 * will occur. Child fragments within child fragments should usually be avoided. However, if another
 * level of child fragment is required, then another scope would need to be created (perhaps
 * PerGrandChild custom scope annotation).
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerChildFragment