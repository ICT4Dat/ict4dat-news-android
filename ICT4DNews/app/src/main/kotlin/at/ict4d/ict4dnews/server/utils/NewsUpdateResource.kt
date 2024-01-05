package at.ict4d.ict4dnews.server.utils

import at.ict4d.ict4dnews.models.Blog

data class NewsUpdateResource(
    val status: Status,
    val currentItem: Resource<Blog>?,
    val successfulBlogs: List<Blog>,
    val failedBlogs: List<Blog>,
    val totalCount: Int,
)
