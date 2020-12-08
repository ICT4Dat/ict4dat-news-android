package at.ict4d.ict4dnews.server.api

import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiJsonSelfHostedWPService {

    @GET
    suspend fun getJsonNewsOfUrl(
        @Url url: String,
        @Query("per_page") numberOfNewsToRequest: Int = 5, // get 5 news posts per default
        @Query("after") newsAfterDate: String
    ): Response<List<SelfHostedWPPost>>

    @GET
    suspend fun getJsonNewsAuthorByID(
        @Url url: String
    ): Response<WordpressAuthor>

    @GET
    suspend fun getJsonNewsMediaForPost(
        @Url url: String
    ): Response<List<WordpressMedia>>
}
