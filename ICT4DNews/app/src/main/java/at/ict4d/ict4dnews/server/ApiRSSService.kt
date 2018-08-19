package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.models.rss.RSSFeed
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiRSSService {

    @GET
    fun getRssNews(@Url url: String): Single<RSSFeed>
}