package at.ict4d.ict4dnews.persistence

import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import at.ict4d.ict4dnews.persistence.database.dao.SelfHostedWPPostDao
import at.ict4d.ict4dnews.persistence.database.dao.WordpressAuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.WordpressMediaDao
import io.reactivex.Flowable
import javax.inject.Inject

class PersistenceManager @Inject constructor(
        private val selfHostedWPPostDao: SelfHostedWPPostDao,
        private val wordpressAuthorDao: WordpressAuthorDao,
        private val wordpressMediaDao: WordpressMediaDao
) : IPersistenceManager {

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