package at.ict4d.ict4dnews.persistence

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.persistence.database.dao.SelfHostedWPPostDao
import at.ict4d.ict4dnews.persistence.database.dao.WordpressAuthorDao
import javax.inject.Inject

class PersistenceManager : IPersistenceManager {

    @Inject
    protected lateinit var selfHostedWPPostDao: SelfHostedWPPostDao

    @Inject
    protected lateinit var wordpressAuthorDao: WordpressAuthorDao

    init {
        ICT4DNewsApplication.component.inject(this)
    }

    // Wordpress Authors

    override fun insertAuthor(author: WordpressAuthor) = wordpressAuthorDao.insert(author)

    override fun insertAllAuthors(authors: List<WordpressAuthor>) = wordpressAuthorDao.insertAll(authors)

    override fun getAllAuthors(): LiveData<List<WordpressAuthor>> = wordpressAuthorDao.getAll()

    // Self Hosted Wordpress Blog

    override fun insertSelfHostedWPPost(post: SelfHostedWPPost) = selfHostedWPPostDao.insert(post)

    override fun insertAllSelfHostedWPPosts(posts: List<SelfHostedWPPost>) = selfHostedWPPostDao.insertAll(posts)

    override fun getAllSelfHostedWPPosts(): LiveData<List<SelfHostedWPPost>> = selfHostedWPPostDao.getAll()

}