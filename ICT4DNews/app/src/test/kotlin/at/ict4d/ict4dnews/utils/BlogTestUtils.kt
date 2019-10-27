package at.ict4d.ict4dnews.utils

import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.FeedType
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao

fun generateFeedType(): FeedType = when (generateRandomNumber(3)) {
    1 -> FeedType.RSS
    2 -> FeedType.SELF_HOSTED_WP_BLOG
    else -> FeedType.WORDPRESS_COM
}

fun generateActive(): Boolean = when (generateRandomNumber(2)) {
    1 -> true
    2 -> false
    else -> true
}

fun generateBlog(active: Boolean = generateActive()) = Blog(
    generateRandomURL(),
    generateRandomURL(),
    generateUUID(),
    generateUUID(),
    generateFeedType(),
    generateRandomURL(),
    active
)

fun generateBlogAndInsert(blogDao: BlogDao, active: Boolean = generateActive()): Blog {
    val blog = generateBlog(active)
    blogDao.insert(blog)
    return blog
}

fun generateListBlogAndInsert(blogDao: BlogDao, numberOfBlogs: Int = 5): List<Blog> {
    val list = mutableListOf<Blog>()
    for (i in 0 until numberOfBlogs) {
        list.add(generateBlogAndInsert(blogDao))
    }
    return list
}
