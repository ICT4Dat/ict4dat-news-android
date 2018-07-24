package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.models.rss.RSSFeed
import io.reactivex.Flowable
import retrofit2.http.GET

interface ApiRSSService {

    @GET("http://www.ict4d.at/feed/")
    fun getRssICT4DatNews(): Flowable<RSSFeed>
}