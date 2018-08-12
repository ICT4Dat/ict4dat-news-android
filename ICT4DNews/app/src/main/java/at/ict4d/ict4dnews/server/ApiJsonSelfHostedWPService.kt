package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiJsonSelfHostedWPService {

    @GET
    fun getJsonNewsOfURL(
        @Url url: String
    ): Single<List<SelfHostedWPPost>>

    @GET("http://www.ict4d.at/wp-json/wp/v2/posts")
    fun getJsonICT4DatNews(
        @Query("per_page") numberOfNewsToRequest: Int = 20, // get 20 news posts per default
        @Query("after") newsAfterDate: String
    ): Single<List<SelfHostedWPPost>>

    @GET("http://www.ict4d.at/wp-json/wp/v2/users/{serverAuthorID}/")
    fun getJsonICT4DatAuthorByID(@Path("serverAuthorID") serverAuthorID: Int): Call<WordpressAuthor>

    @GET("http://www.ict4d.at/wp-json/wp/v2/media")
    fun getJsonICT4DatMedia(): Single<List<WordpressMedia>>

    @GET("http://www.ict4d.at/wp-json/wp/v2/media")
    fun getJsonICT4DatMediaForPost(@Query("parent") serverPostID: Int): Call<List<WordpressMedia>>
}