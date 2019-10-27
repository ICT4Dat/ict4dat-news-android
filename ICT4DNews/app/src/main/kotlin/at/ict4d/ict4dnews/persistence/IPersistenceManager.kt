package at.ict4d.ict4dnews.persistence

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.Media
import at.ict4d.ict4dnews.models.News
import com.f2prateek.rx.preferences2.Preference
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

interface IPersistenceManager {

    // Shared Preferences

    fun getLastAutomaticNewsUpdateLocalDate(): Preference<LocalDate>

    fun isAutomaticNewsUpdateEnabled(): Preference<Boolean>

    fun isBugTrackingEnabled(): Preference<Boolean>

    // Authors

    fun getAuthorBy(authorId: String): LiveData<Author?>

    // News

    fun getLatestNewsPublishedDate(blogID: String): LocalDateTime

    fun getLatestNewsPublishedDate(): LocalDateTime

    fun getAllActiveNews(query: String): DataSource.Factory<Int, News>

    fun requestLatestNewsByDate(recentNewsDate: LocalDateTime): List<News>

    fun getCountOfNews(): Int

    // Blogs

    fun insertAll(blogs: List<Blog>): List<Long>

    fun getAllBlogs(): LiveData<List<Blog>>

    fun getAllBlogsAsList(): List<Blog>

    fun getAllActiveBlogs(): List<Blog>

    fun getBlogByUrlAsLiveData(url: String): LiveData<Blog?>

    fun getBlogByUrl(feedUrl: String): Blog?

    fun updateBlog(blog: Blog): Int

    fun isBlogsExist(): Boolean

    fun getBlogsCountAsLiveData(): LiveData<Int>

    fun getActiveBlogsCountAsLiveData(): LiveData<Int>

    // Transactions
    fun insertAuthorsNewsAndMedia(authors: List<Author>, news: List<News>, media: List<Media>)
}
