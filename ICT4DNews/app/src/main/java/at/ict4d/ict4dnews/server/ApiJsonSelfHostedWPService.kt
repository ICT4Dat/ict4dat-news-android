package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import io.reactivex.Flowable
import retrofit2.http.GET

interface ApiJsonSelfHostedWPService {

    @GET("http://www.ict4d.at/wp-json/wp/v2/posts")
    fun getJsonICT4DatNews(): Flowable<List<SelfHostedWPPost>>

    @GET("http://www.ict4d.at/wp-json/wp/v2/users")
    fun getJsonICT4DatAuthors(): Flowable<List<WordpressAuthor>>

}