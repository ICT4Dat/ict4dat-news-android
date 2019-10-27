package at.ict4d.ict4dnews.server

import io.reactivex.disposables.Disposable

/**
 * This interface is used to offer functionality to connect to
 * all APIs the application is using.
 * It handles connections to RSS Feeds, Self Hosted Wordpress Blogs
 * and the ICT4D.at server.
 */
interface IServer {

    /**
     * Loads all active [blogs][at.ict4d.ict4dnews.models.Blog] from the respected servers and stores the result
     * into the database.
     * Trigger this function for an update and subscribe to the database for changes.
     *
     * @return The RXJava [Disposable] to dispose this call when not needed anymore.
     */
    fun loadAllNewsFromAllActiveBlogs(): Disposable

    /**
     * Loads all active [blogs][at.ict4d.ict4dnews.models.Blog] from the respected servers and stores the result
     * into the database. This call is synchronous and will block the thread on which it got called.
     * Trigger this function for an update and subscribe to the database for changes.
     *
     * @return True if the operation was successful, otherwise false.
     */
    fun loadAllNewsFromAllActiveBlogsSynchronous(): Boolean

    /**
     * Loads all available [blogs][at.ict4d.ict4dnews.models.Blog] in the application.
     *
     * @return The RXJava [Disposable] to dispose this call when not needed anymore.
     */
    fun loadBlogs(): Disposable
}
