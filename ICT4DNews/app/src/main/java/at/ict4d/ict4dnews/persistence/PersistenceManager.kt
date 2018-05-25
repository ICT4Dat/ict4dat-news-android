package at.ict4d.ict4dnews.persistence

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import at.ict4d.ict4dnews.persistence.database.dao.SelfHostedWPPostDao
import at.ict4d.ict4dnews.persistence.database.dao.WordpressAuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.WordpressMediaDao
import io.reactivex.Flowable
import javax.inject.Inject

class PersistenceManager : IPersistenceManager {

    @Inject
    protected lateinit var selfHostedWPPostDao: SelfHostedWPPostDao

    @Inject
    protected lateinit var wordpressAuthorDao: WordpressAuthorDao

    @Inject
    protected lateinit var wordpressMediaDao: WordpressMediaDao

    init {
        ICT4DNewsApplication.component.inject(this)
    }

    // Self Hosted Wordpress Authors

    override fun insertWordpressAuthor(author: WordpressAuthor) = wordpressAuthorDao.insert(author)

    override fun insertAllWordpressAuthors(authors: List<WordpressAuthor>) = wordpressAuthorDao.insertAll(authors)

    override fun getAllWordpressAuthors(): Flowable<List<WordpressAuthor>> = wordpressAuthorDao.getAll()

    // Self Hosted Wordpress Blog

    override fun insertSelfHostedWPPost(post: SelfHostedWPPost) = selfHostedWPPostDao.insert(post)

    override fun insertAllSelfHostedWPPosts(posts: List<SelfHostedWPPost>) = selfHostedWPPostDao.insertAll(posts)

    override fun getAllSelfHostedWPPosts(): Flowable<List<SelfHostedWPPost>> = selfHostedWPPostDao.getAll()

    // Self Hosted Wordpress Media

    override fun insertWordpressMedia(media: WordpressMedia) = wordpressMediaDao.insert(media)

    override fun insertAllWordpressMedia(media: List<WordpressMedia>) = wordpressMediaDao.insertAll(media)

    override fun getAllWordpressMedia(): Flowable<List<WordpressMedia>> = wordpressMediaDao.getAll()

}