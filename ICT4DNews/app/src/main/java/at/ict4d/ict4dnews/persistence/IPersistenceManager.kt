package at.ict4d.ict4dnews.persistence

import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import io.reactivex.Flowable

interface IPersistenceManager {

    // Self Hosted Wordpress Authors

    fun insertWordpressAuthor(author: WordpressAuthor)

    fun insertAllWordpressAuthors(authors: List<WordpressAuthor>)

    fun getAllWordpressAuthors(): Flowable<List<WordpressAuthor>>


    // Self Hosted Wordpress Blog

    fun insertSelfHostedWPPost(post: SelfHostedWPPost)

    fun insertAllSelfHostedWPPosts(posts: List<SelfHostedWPPost>)

    fun getAllSelfHostedWPPosts(): Flowable<List<SelfHostedWPPost>>


    // Self Hosted Wordpress Media

    fun insertWordpressMedia(media: WordpressMedia)

    fun insertAllWordpressMedia(media: List<WordpressMedia>)

    fun getAllWordpressMedia(): Flowable<List<WordpressMedia>>

}