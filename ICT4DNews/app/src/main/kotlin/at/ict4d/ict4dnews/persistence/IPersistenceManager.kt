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

    fun insertAuthor(author: Author): Long

    fun insertAllAuthors(authors: List<Author>): List<Long>

    fun getAllAuthors(): LiveData<List<Author>>

    fun getAuthorBy(authorId: String): LiveData<Author>

    // News

    fun insertNews(news: News): Long

    fun insertAllNews(news: List<News>): List<Long>

    fun getAllOrderedByPublishedDate(): LiveData<List<News>>

    fun getLatestNewsPublishedDate(blogID: String): LocalDateTime

    fun getLatestNewsPublishedDate(): LocalDateTime

    fun getAllActiveNews(): LiveData<List<News>>

    fun requestLatestNewsByDate(recentNewsDate: LocalDateTime): List<News>

    fun getAllActiveNewsAsFlowable(): Flowable<List<News>>

    fun getCountOfNews(): Int

    // Media

    fun insertMedia(media: Media): Long

    fun insertAllMedia(media: List<Media>): List<Long>

    fun getAllMedia(): LiveData<List<Media>>

    // Blogs

    fun insert(blog: Blog): Long

    fun insertAll(blogs: List<Blog>): List<Long>

    fun getAllBlogs(): LiveData<List<Blog>>

    fun getAllBlogsAsList(): List<Blog>

    fun getAllActiveBlogs(): List<Blog>

    fun getBlogURLByFuzzyURL(fuzzyURL: String): String?

    fun getBlogByURL(url: String): LiveData<Blog>

    fun updateBlog(blog: Blog)

    fun getAllActiveBlogsAsFlowable(): Flowable<List<Blog>>

    fun isBlogsExist(): Boolean

    fun getBlogsCountAsLiveData(): LiveData<Int>

    fun getActiveBlogsCountAsLiveData(): LiveData<Int>

    // Transactions
    fun insertAuthorsNewsAndMedia(authors: List<Author>, news: List<News>, media: List<Media>)
}