package at.ict4d.ict4dnews.screens.splashscreen

import androidx.lifecycle.LiveData
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.persistence.IPersistenceManager
import at.ict4d.ict4dnews.screens.base.BaseViewModel
import at.ict4d.ict4dnews.server.IServer
import org.jetbrains.anko.doAsync
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val persistenceManager: IPersistenceManager,
    private val server: IServer
) : BaseViewModel() {

    val allBlogs: LiveData<List<Blog>> = persistenceManager.getAllBlogs()

    init {
        doAsync { downloadBlogsIfNotExist() }
    }

    private fun downloadBlogsIfNotExist() {
        if (!persistenceManager.isBlogsExist()) {
            server.loadBlogs()
        }
    }
}