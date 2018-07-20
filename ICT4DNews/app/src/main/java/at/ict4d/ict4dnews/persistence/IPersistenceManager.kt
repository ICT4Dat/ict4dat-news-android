package at.ict4d.ict4dnews.persistence

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.AuthorModel
import at.ict4d.ict4dnews.models.MediaModel
import at.ict4d.ict4dnews.models.NewsModel
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import io.reactivex.Flowable

interface IPersistenceManager {

    // Self Hosted Wordpress Authors

    fun insertAuthor(author: AuthorModel)

    fun insertAllAuthors(authors: List<AuthorModel>)

    fun getAllAuthors(): LiveData<List<AuthorModel>>


    // Self Hosted Wordpress Blog

    fun insertNews(news: NewsModel)

    fun insertAllNews(news: List<NewsModel>)

    fun getAllNews(): LiveData<List<NewsModel>>


    // Self Hosted Wordpress Media

    fun insertMedia(media: MediaModel)

    fun insertAllMedia(media: List<MediaModel>)

    fun getAllMedia(): LiveData<List<MediaModel>>

}