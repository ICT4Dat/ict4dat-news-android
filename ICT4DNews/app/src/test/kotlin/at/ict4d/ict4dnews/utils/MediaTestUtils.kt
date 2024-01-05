package at.ict4d.ict4dnews.utils

import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Media
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao

fun generateMedia(
    news: News,
    author: Author,
) = Media(
    generateUUID(),
    generateRandomNumber(),
    news.link,
    author.link,
    generateUUID(),
    generateUUID(),
    generateUUID(),
    generatePastLocalDateTime(),
)

fun generateMediaAndInsert(
    mediaDao: MediaDao,
    authorDao: AuthorDao,
    blogDao: BlogDao,
    newsDao: NewsDao,
): Media {
    val author = generateAuthorAndInsert(authorDao)
    val media = generateMedia(generateNewsAndInsert(newsDao, generateBlogAndInsert(blogDao), author), author)
    mediaDao.insert(media)
    return media
}

fun generateMediaListAndInsert(
    mediaDao: MediaDao,
    authorDao: AuthorDao,
    blogDao: BlogDao,
    newsDao: NewsDao,
    numberOfMedia: Int = 5,
): List<Media> {
    val list = mutableListOf<Media>()
    for (i in 0 until numberOfMedia) {
        list.add(generateMediaAndInsert(mediaDao, authorDao, blogDao, newsDao))
    }
    return list
}
