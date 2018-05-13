package at.ict4d.ict4dnews.persistence

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor

interface IPersistenceManager {

    // Wordpress Authors

    fun insertAuthor(author: WordpressAuthor)

    fun insertAllAuthors(authors: List<WordpressAuthor>)

    fun getAllAuthors(): LiveData<List<WordpressAuthor>>


    // Self Hosted Wordpress Blog

    fun insertSelfHostedWPPost(post: SelfHostedWPPost)

    fun insertAllSelfHostedWPPosts(posts: List<SelfHostedWPPost>)

    fun getAllSelfHostedWPPosts(): LiveData<List<SelfHostedWPPost>>

}