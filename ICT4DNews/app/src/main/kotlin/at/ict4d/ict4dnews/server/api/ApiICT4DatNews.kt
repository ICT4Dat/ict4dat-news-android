package at.ict4d.ict4dnews.server.api

import at.ict4d.ict4dnews.models.Blog
import retrofit2.Response
import retrofit2.http.GET

interface ApiICT4DatNews {
    @GET("blogs/v5/ict4d_blogs.php")
    suspend fun getBlogs(): Response<List<Blog>>
}
