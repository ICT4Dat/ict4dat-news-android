package at.ict4d.ict4dnews.persistence

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Media
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class PersistenceManager @Inject constructor(
    private val authorDao: AuthorDao,
    private val newsDao: NewsDao,
    private val mediaDao: MediaDao
) : IPersistenceManager {

    // Authors

    override fun insertAuthor(author: Author) = authorDao.insert(author)

    override fun insertAllAuthors(authors: List<Author>) = authorDao.insertAll(authors)

    override fun getAllAuthors(): LiveData<List<Author>> = authorDao.getAll()

    // News

    override fun insertNews(news: News) = newsDao.insert(news)

    override fun insertAllNews(news: List<News>) = newsDao.insertAll(news)

    override fun getAllOrderedByPublishedDate(): LiveData<List<News>> = newsDao.getAllOrderedByPublishedDate()

    override fun getLatestNewsPublishedDate(): LocalDateTime = newsDao.getLatestNewsPublishedDate()
        ?: LocalDateTime.now().minusYears(10) // if database is empty then today minus 10 years per default

    // Media

    override fun insertMedia(media: Media) = mediaDao.insert(media)

    override fun insertAllMedia(media: List<Media>) = mediaDao.insertAll(media)

    override fun getAllMedia(): LiveData<List<Media>> = mediaDao.getAll()
}