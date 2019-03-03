package at.ict4d.ict4dnews.utils

import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.FeedType
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao

fun generateBlogName() = "Blog ${generateRandomNumber()}"

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

fun generateBlog() = Blog(generateRandomURL(), generateRandomURL(), generateBlogName(), generateDescription(), generateFeedType(), generateRandomURL(), generateActive())

fun generateBlogAndInsert(blogDao: BlogDao): Blog {
    val blog = generateBlog()
    blogDao.insert(blog)
    return blog
}