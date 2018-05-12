package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import io.reactivex.Flowable
import retrofit2.http.GET

interface ApiJsonSelfHostedWPService {

    @GET("http://www.ict4d.at/wp-json/wp/v2/posts")
    fun getJSONICT4DatNews(): Flowable<List<SelfHostedWPPost>>

}