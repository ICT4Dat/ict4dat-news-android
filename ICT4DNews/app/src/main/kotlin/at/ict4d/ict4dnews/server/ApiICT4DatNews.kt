package at.ict4d.ict4dnews.server

import at.ict4d.ict4dnews.models.Blog
import io.reactivex.Single
import retrofit2.http.GET

interface ApiICT4DatNews {

    @GET("blogs/v1/ict4d_blogs.php")
    fun getBlogs(): Single<List<Blog>>
}