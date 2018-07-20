package at.ict4d.ict4dnews.persistence

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.models.AuthorModel
import at.ict4d.ict4dnews.models.MediaModel
import at.ict4d.ict4dnews.models.NewsModel
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
import io.reactivex.Flowable
import javax.inject.Inject

class PersistenceManager : IPersistenceManager {

    @Inject
    protected lateinit var newsDao: NewsDao

    @Inject
    protected lateinit var authorDao: AuthorDao

    @Inject
    protected lateinit var mediaDao: MediaDao

    init {
        ICT4DNewsApplication.component.inject(this)
    }

    // Self Hosted Wordpress Authors

    override fun insertAuthor(author: AuthorModel) = authorDao.insert(author)

    override fun insertAllAuthors(authors: List<AuthorModel>) = authorDao.insertAll(authors)

    override fun getAllAuthors(): LiveData<List<AuthorModel>> = authorDao.getAll()

    // Self Hosted Wordpress Blog

    override fun insertNews(news: NewsModel) = newsDao.insert(news)

    override fun insertAllNews(news: List<NewsModel>) = newsDao.insertAll(news)

    override fun getAllNews(): LiveData<List<NewsModel>> = newsDao.getAll()

    // Self Hosted Wordpress Media

    override fun insertMedia(media: MediaModel) = mediaDao.insert(media)

    override fun insertAllMedia(media: List<MediaModel>) = mediaDao.insertAll(media)

    override fun getAllMedia(): LiveData<List<MediaModel>> = mediaDao.getAll()

}