package at.ict4d.ict4dnews.persistence

import android.arch.lifecycle.LiveData
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia

interface IPersistenceManager {

    // Self Hosted Wordpress Authors

    fun insertWordpressAuthor(author: WordpressAuthor)

    fun insertAllWordpressAuthors(authors: List<WordpressAuthor>)

    fun getAllWordpressAuthors(): LiveData<List<WordpressAuthor>>


    // Self Hosted Wordpress Blog

    fun insertSelfHostedWPPost(post: SelfHostedWPPost)

    fun insertAllSelfHostedWPPosts(posts: List<SelfHostedWPPost>)

    fun getAllSelfHostedWPPosts(): LiveData<List<SelfHostedWPPost>>


    // Self Hosted Wordpress Media

    fun insertWordpressMedia(media: WordpressMedia)

    fun insertAllWordpressMedia(media: List<WordpressMedia>)

    fun getAllWordpressMedia(): LiveData<List<WordpressMedia>>

}