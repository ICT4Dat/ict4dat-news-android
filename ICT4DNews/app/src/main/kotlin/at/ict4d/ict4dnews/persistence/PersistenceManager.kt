package at.ict4d.ict4dnews.persistence

import androidx.lifecycle.LiveData
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
import com.f2prateek.rx.preferences2.Preference
import io.reactivex.Flowable
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

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

    override fun getNewsServiceId(): Preference<String> = sharedPrefs.newsServiceId

    // Authors

    override fun insertAuthor(author: Author) = authorDao.insert(author)

    override fun insertAllAuthors(authors: List<Author>) = authorDao.insertAll(authors)

    override fun getAllAuthors(): LiveData<List<Author>> = authorDao.getAll()

    override fun getAuthorBy(authorId: String): LiveData<Author> = authorDao.getAuthorDetailsBy(authorId)

    // News

    override fun insertNews(news: News) = newsDao.insert(news)

    override fun insertAllNews(news: List<News>) = newsDao.insertAll(news)

    override fun getAllOrderedByPublishedDate(): LiveData<List<News>> = newsDao.getAllOrderedByPublishedDate()

    override fun getLatestNewsPublishedDate(blogID: String): LocalDateTime = newsDao.getLatestBlogPublishedDate(blogID) ?: LocalDateTime.now().minusYears(10) // if database is empty then today minus 10 years per default

    override fun getAllActiveNews(): LiveData<List<News>> = newsDao.getAllActiveNews()

    override fun getAllActiveNewsAsFlowable(): Flowable<List<News>> = newsDao.getAllActiveNewsAsFlowable()

    override fun getCountOfNews(): Int = newsDao.getCountOfNews()

    // Media

    override fun insertMedia(media: Media) = mediaDao.insert(media)

    override fun insertAllMedia(media: List<Media>) = mediaDao.insertAll(media)

    override fun getAllMedia(): LiveData<List<Media>> = mediaDao.getAll()

    // Blogs

    override fun insert(blog: Blog) = blogsDao.insert(blog)

    override fun insertAll(blogs: List<Blog>) = blogsDao.insertAll(blogs)

    override fun getAllBlogs(): LiveData<List<Blog>> = blogsDao.getAll()

    override fun getAllBlogsAsList(): List<Blog> = blogsDao.getAllBlogsAsList()

    override fun getAllActiveBlogs(): List<Blog> = blogsDao.getAllActiveBlogs()

    override fun getBlogURLByFuzzyURL(fuzzyURL: String) = blogsDao.getBlogURLByFuzzyURL(fuzzyURL)

    override fun getBlogByURL(url: String): LiveData<Blog> = blogsDao.getBlogByURL(url)

    override fun updateBlog(blog: Blog) = blogsDao.updateBlog(blog)

    override fun getAllActiveBlogsAsFlowable(): Flowable<List<Blog>> = blogsDao.getAllActiveBlogsAsFlowable()

    override fun isBlogsExist(): Boolean = blogsDao.isBlogsExist()

    // Transactions
    override fun insertAuthorsNewsAndMedia(authors: List<Author>, news: List<News>, media: List<Media>) {
        database.runInTransaction {
            authorDao.insertAll(authors)
            newsDao.insertAll(news)
            mediaDao.insertAll(media)
        }
    }
}