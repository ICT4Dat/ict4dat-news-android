package at.ict4d.ict4dnews.dao

import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import at.ict4d.ict4dnews.utils.generateAuthorAndInsert
import at.ict4d.ict4dnews.utils.generateBlogAndInsert
import at.ict4d.ict4dnews.utils.generateNews
import at.ict4d.ict4dnews.utils.generateNewsListAndInsert
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NewsDaoTests : BaseDaoTest() {

    private lateinit var newsDao: NewsDao
    private lateinit var blogDao: BlogDao
    private lateinit var authorDao: AuthorDao

    @Before
    override fun createDatabase() {
        super.createDatabase()
        newsDao = appDatabase.newsDao()
        blogDao = appDatabase.blogDao()
        authorDao = appDatabase.authorDao()
    }

    @Test
    fun testInsert() {

        val blog = generateBlogAndInsert(blogDao)
        val author = generateAuthorAndInsert(authorDao)

        val news = generateNews(blog, author)
        val index = newsDao.insert(news)
        Assert.assertEquals(1L, index)
    }

    @Test
    fun testInsertAll() {

        val list = generateNewsListAndInsert(newsDao, generateBlogAndInsert(blogDao), generateAuthorAndInsert(authorDao))

        val rowIndexes = newsDao.insertAll(list)
        Assert.assertEquals(list.size, rowIndexes.size)
    }
}