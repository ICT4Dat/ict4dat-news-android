package at.ict4d.ict4dnews.persistence

import androidx.lifecycle.LiveData
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.Media
import at.ict4d.ict4dnews.models.News
import com.f2prateek.rx.preferences2.Preference
import io.reactivex.Flowable
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

interface IPersistenceManager {

    // Shared Preferences

    fun getLastAutomaticNewsUpdateLocalDate(): Preference<LocalDate>

    // Authors

    fun insertAuthor(author: Author)

    fun insertAllAuthors(authors: List<Author>)

    fun getAllAuthors(): LiveData<List<Author>>

    fun getAuthorBy(authorId: String): LiveData<Author>

    // News

    fun insertNews(news: News)

    fun insertAllNews(news: List<News>)

    fun getAllOrderedByPublishedDate(): LiveData<List<News>>

    fun getLatestNewsPublishedDate(blogID: String): LocalDateTime

    fun getLatestNewsPublishedDate(): LocalDateTime

    fun getAllActiveNews(): LiveData<List<News>>

    fun requestLatestNewsByDate(recentNewsDate: LocalDateTime): Int

    fun getAllActiveNewsAsFlowable(): Flowable<List<News>>

    fun getCountOfNews(): Int

    // Media

    fun insertMedia(media: Media)

    fun insertAllMedia(media: List<Media>)

    fun getAllMedia(): LiveData<List<Media>>

    // Blogs

    fun insert(blog: Blog)

    fun insertAll(blogs: List<Blog>)

    fun getAllBlogs(): LiveData<List<Blog>>

    fun getAllBlogsAsList(): List<Blog>

    fun getAllActiveBlogs(): List<Blog>

    fun getBlogURLByFuzzyURL(fuzzyURL: String): String?

    fun getBlogByURL(url: String): LiveData<Blog>

    fun updateBlog(blog: Blog)

    fun getAllActiveBlogsAsFlowable(): Flowable<List<Blog>>

    fun isBlogsExist(): Boolean

    // Transactions
    fun insertAuthorsNewsAndMedia(authors: List<Author>, news: List<News>, media: List<Media>)
}