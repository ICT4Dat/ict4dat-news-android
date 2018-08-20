package at.ict4d.ict4dnews.persistence

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.Media
import at.ict4d.ict4dnews.models.News
import org.threeten.bp.LocalDateTime

interface IPersistenceManager {

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

    // Media

    fun insertMedia(media: Media)

    fun insertAllMedia(media: List<Media>)

    fun getAllMedia(): LiveData<List<Media>>

    // Blogs

    fun insert(blog: Blog)

    fun insertAll(blogs: List<Blog>)

    fun getAll(): LiveData<List<Blog>>

    fun getAllActiveBlogs(): List<Blog>

    fun getBlogURLByFuzzyURL(fuzzyURL: String): String?
}