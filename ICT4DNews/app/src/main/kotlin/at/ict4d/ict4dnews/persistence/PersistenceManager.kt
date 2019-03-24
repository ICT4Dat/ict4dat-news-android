package at.ict4d.ict4dnews.persistence

import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.Media
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.database.AppDatabase
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import at.ict4d.ict4dnews.persistence.sharedpreferences.ISharedPrefs
import org.threeten.bp.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersistenceManager @Inject constructor(
    private val database: AppDatabase,
    private val sharedPrefs: ISharedPrefs,
    private val authorDao: AuthorDao,
    private val newsDao: NewsDao,
    private val mediaDao: MediaDao,
    private val blogsDao: BlogDao
) : IPersistenceManager {

    // Shared Preferences

    override fun getLastAutomaticNewsUpdateLocalDate() = sharedPrefs.lastAutomaticNewsUpdateLocalDate

    // Authors

    override fun getAuthorBy(authorId: String) = authorDao.getAuthorDetailsBy(authorId)

    // News

    override fun getLatestNewsPublishedDate(blogID: String): LocalDateTime =
        newsDao.getLatestPublishedDateOfBlog(blogID)
            ?: LocalDateTime.now().minusYears(10) // if database is empty then today minus 10 years per default

    override fun getLatestNewsPublishedDate(): LocalDateTime = newsDao.getLatestNewsPublishedDate()
        ?: LocalDateTime.now().minusYears(10) // if database is empty then today minus 10 years per default

    override fun requestLatestNewsByDate(recentNewsDate: LocalDateTime) = newsDao.getLatestNewsByDate(recentNewsDate)

    override fun getAllActiveNews(query: String) = newsDao.getAllActiveNews(query)

    override fun getCountOfNews(): Int = newsDao.getCountOfNews()

    // Blogs

    override fun insertAll(blogs: List<Blog>) = blogsDao.insertAll(blogs)

    override fun getAllBlogs() = blogsDao.getAll()

    override fun getAllBlogsAsList() = blogsDao.getAllBlogsAsList()

    override fun getAllActiveBlogs() = blogsDao.getAllActiveBlogs()

    override fun getBlogByUrlAsLiveData(url: String) = blogsDao.getBlogByUrlAsLiveData(url)

    override fun getBlogByUrl(feedUrl: String) = blogsDao.getBlogByUrl(feedUrl)

    override fun updateBlog(blog: Blog) = blogsDao.updateBlog(blog)

    override fun isBlogsExist(): Boolean = blogsDao.isBlogsExist()

    override fun getBlogsCountAsLiveData() = blogsDao.getBlogsCountAsLiveData()

    override fun getActiveBlogsCountAsLiveData() = blogsDao.getActiveBlogsCountAsLiveData()

    // Transactions
    override fun insertAuthorsNewsAndMedia(authors: List<Author>, news: List<News>, media: List<Media>) {
        database.runInTransaction {
            authorDao.insertAll(authors)
            newsDao.insertAll(news)
            mediaDao.insertAll(media)
        }
    }
}