package at.ict4d.ict4dnews.server.api

import at.ict4d.ict4dnews.models.rss.RSSFeed
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiRssService {
    @GET
    suspend fun getRssNews(
        @Url url: String,
    ): Response<RSSFeed>
}
