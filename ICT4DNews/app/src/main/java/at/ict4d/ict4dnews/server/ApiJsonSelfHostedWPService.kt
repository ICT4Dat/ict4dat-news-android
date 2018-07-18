package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import io.reactivex.Flowable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiJsonSelfHostedWPService {

    @GET("http://www.ict4d.at/wp-json/wp/v2/posts?per_page=30")
    fun getJsonICT4DatNews(): Flowable<List<SelfHostedWPPost>>

    @GET("http://www.ict4d.at/wp-json/wp/v2/users")
    fun getJsonICT4DatAuthors(): Flowable<List<WordpressAuthor>>

    @GET("http://www.ict4d.at/wp-json/wp/v2/media")
    fun getJsonICT4DatMedia(): Flowable<List<WordpressMedia>>

    @GET("http://www.ict4d.at/wp-json/wp/v2/media")
    fun getJsonICT4DatMediaForPost(@Query("parent") serverPostID: Int): Call<List<WordpressMedia>>
}