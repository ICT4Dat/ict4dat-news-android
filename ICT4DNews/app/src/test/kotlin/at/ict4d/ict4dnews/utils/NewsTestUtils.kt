package at.ict4d.ict4dnews.utils

import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao

fun generateTitle() = "Title ${generateRandomNumber()}"

fun generateDescription() = "Description ${generateRandomNumber()}"

fun generateNews(blog: Blog, author: Author): News {
    return News(generateUUID(), author.link, generateRandomNumber(), generateRandomURL(), generateTitle(), generateDescription(), generatePastLocalDateTime(), blog.feed_url)
}

fun generateNewsListAndInsert(newsDao: NewsDao, blog: Blog, author: Author, numberOfNews: Int = 5): List<News> {

    val newsList = mutableListOf<News>()
    for (i in 0 until numberOfNews) {
        newsList.add(generateNews(blog, author))
    }

    newsDao.insertAll(newsList)
    return newsList
}