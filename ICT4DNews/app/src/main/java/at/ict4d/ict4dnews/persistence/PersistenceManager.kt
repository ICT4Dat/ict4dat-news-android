package at.ict4d.ict4dnews.persistence

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.AuthorModel
import at.ict4d.ict4dnews.models.MediaModel
import at.ict4d.ict4dnews.models.NewsModel
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import javax.inject.Inject

class PersistenceManager @Inject constructor(
    private val authorDao: AuthorDao,
    private val newsDao: NewsDao,
    private val mediaDao: MediaDao
) : IPersistenceManager {

    // Authors

    override fun insertAuthor(author: AuthorModel) = authorDao.insert(author)

    override fun insertAllAuthors(authors: List<AuthorModel>) = authorDao.insertAll(authors)

    override fun getAllAuthors(): LiveData<List<AuthorModel>> = authorDao.getAll()

    // News

    override fun insertNews(news: NewsModel) = newsDao.insert(news)

    override fun insertAllNews(news: List<NewsModel>) = newsDao.insertAll(news)

    override fun getAllNews(): LiveData<List<NewsModel>> = newsDao.getAll()

    // Media

    override fun insertMedia(media: MediaModel) = mediaDao.insert(media)

    override fun insertAllMedia(media: List<MediaModel>) = mediaDao.insertAll(media)

    override fun getAllMedia(): LiveData<List<MediaModel>> = mediaDao.getAll()
}