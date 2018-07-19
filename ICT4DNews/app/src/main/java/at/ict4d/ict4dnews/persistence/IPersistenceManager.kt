package at.ict4d.ict4dnews.persistence

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Media
import at.ict4d.ict4dnews.models.News

interface IPersistenceManager {

    // Self Hosted Wordpress Authors

    fun insertAuthor(author: Author)

    fun insertAllAuthors(authors: List<Author>)

    fun getAllAuthors(): LiveData<List<Author>>


    // Self Hosted Wordpress Blog

    fun insertNews(news: News)

    fun insertAllNews(news: List<News>)

    fun getAllOrderedByPublishedDate(): LiveData<List<News>>


    // Self Hosted Wordpress Media

    fun insertMedia(media: Media)

    fun insertAllMedia(media: List<Media>)

    fun getAllMedia(): LiveData<List<Media>>

}