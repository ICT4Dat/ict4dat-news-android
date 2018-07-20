package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiJsonSelfHostedWPService {

    @GET("http://www.ict4d.at/wp-json/wp/v2/posts?per_page=10")
    fun getJsonICT4DatNews(): Single<List<SelfHostedWPPost>>

    @GET("http://www.ict4d.at/wp-json/wp/v2/users/{serverAuthorID}/")
    fun getJsonICT4DatAuthorByID(@Path("serverAuthorID") serverAuthorID: Int): Call<WordpressAuthor>

    @GET("http://www.ict4d.at/wp-json/wp/v2/media")
    fun getJsonICT4DatMedia(): Single<List<WordpressMedia>>

    @GET("http://www.ict4d.at/wp-json/wp/v2/media")
    fun getJsonICT4DatMediaForPost(@Query("parent") serverPostID: Int): Call<List<WordpressMedia>>

}