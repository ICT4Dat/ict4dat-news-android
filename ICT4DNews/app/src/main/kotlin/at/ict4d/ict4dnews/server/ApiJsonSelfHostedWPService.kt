package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiJsonSelfHostedWPService {

    @GET
    fun getJsonNewsOfUrl(
        @Url url: String,
        @Query("per_page") numberOfNewsToRequest: Int = 5, // get 5 news posts per default
        @Query("after") newsAfterDate: String
    ): Single<List<SelfHostedWPPost>>

    @GET
    fun getJsonNewsOfUrlAsCall(
        @Url url: String,
        @Query("per_page") numberOfNewsToRequest: Int = 5, // get 5 news posts per default
        @Query("after") newsAfterDate: String
    ): Call<List<SelfHostedWPPost>>

    @GET
    fun getJsonNewsAuthorByID(
        @Url url: String
    ): Call<WordpressAuthor>

    @GET
    fun getJsonNewsMediaForPost(
        @Url url: String
    ): Call<List<WordpressMedia>>
}
