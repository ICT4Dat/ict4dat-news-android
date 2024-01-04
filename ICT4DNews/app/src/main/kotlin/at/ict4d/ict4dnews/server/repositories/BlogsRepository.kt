package at.ict4d.ict4dnews.server.repositories

import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.server.api.ApiICT4DatNews
import at.ict4d.ict4dnews.server.utils.Resource
import at.ict4d.ict4dnews.server.utils.resultFlow
import at.ict4d.ict4dnews.utils.recordNetworkBreadcrumb
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.net.HttpURLConnection

class BlogsRepository(
    private val blogsDao: BlogDao,
    private val apiIct4dNews: ApiICT4DatNews,
) {
    /**
     * Loads all available [blogs][at.ict4d.ict4dnews.models.Blog] in the application.
     *
     * @param triggerNetworkRequest If true then a network request is triggered, otherwise only a [Flow] with data from the database is returned
     * @return A [Flow] with a [Resource] to handle the status which contains the list of blogs.
     */
    fun getAllBlogs(triggerNetworkRequest: Boolean = true): Flow<Resource<List<Blog>>> {
        recordNetworkBreadcrumb("loadBlogs", this)

        return if (triggerNetworkRequest) {
            resultFlow(
                databaseQuery = { blogsDao.getAllBlogs() },
                networkCall = { apiIct4dNews.getBlogs() },
                saveCallResult = { newBlogs ->

                    newBlogs.map { blog -> if (blog.logoURL == "null") blog.logoURL = null }

                    val oldBlogs = blogsDao.getAllBlogs().first()
                    newBlogs.map { blog ->
                        blog.active = oldBlogs.find { blog.feed_url == it.feed_url }?.active ?: true
                    }

                    blogsDao.insertAll(newBlogs)
                    Timber.d("downloaded ${newBlogs.size} blogs from ICT4D.at")
                },
            )
        } else {
            blogsDao.getAllBlogs()
                .map { Resource.success(it, HttpURLConnection.HTTP_OK) }
                .flowOn(Dispatchers.IO)
        }
    }

    suspend fun updateBlog(blog: Blog) = blogsDao.updateBlog(blog)

    fun doBlogsExists() = blogsDao.doBlogsExist().flowOn(Dispatchers.IO)

    fun getBlogByUrl(url: String) = blogsDao.getBlogByUrl(url).flowOn(Dispatchers.IO)

    fun getBlogsCount() = blogsDao.getBlogsCount().flowOn(Dispatchers.IO)

    fun getActiveBlogsCount() = blogsDao.getActiveBlogsCount().flowOn(Dispatchers.IO)

    fun getAllActiveBlogs() = blogsDao.getAllActiveBlogs().flowOn(Dispatchers.IO)
}
