package at.ict4d.ict4dnews.dao

import androidx.paging.LivePagedListBuilder
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import at.ict4d.ict4dnews.utils.LiveDataTestUtil
import at.ict4d.ict4dnews.utils.generateAuthorAndInsert
import at.ict4d.ict4dnews.utils.generateBlog
import at.ict4d.ict4dnews.utils.generateBlogAndInsert
import at.ict4d.ict4dnews.utils.generateNews
import at.ict4d.ict4dnews.utils.generateNewsListAndInsert
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.threeten.bp.LocalDateTime

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

        val list =
            generateNewsListAndInsert(newsDao, generateBlogAndInsert(blogDao), generateAuthorAndInsert(authorDao))

        val rowIndexes = newsDao.insertAll(list)
        Assert.assertEquals(list.size, rowIndexes.size)
    }

    @Test
    fun testGetLatestPublishedDateOfBlog() {
        val blog1 = generateBlogAndInsert(blogDao)
        val blog2 = generateBlogAndInsert(blogDao)
        val author = generateAuthorAndInsert(authorDao)

        val list1 = generateNewsListAndInsert(newsDao, blog1, author).sortedBy { it.publishedDate }.reversed()
        val list2 = generateNewsListAndInsert(newsDao, blog2, author).sortedBy { it.publishedDate }.reversed()

        var result = newsDao.getLatestPublishedDateOfBlog(blog1.feed_url)
        Assert.assertNotNull(result)
        Assert.assertEquals(list1.first().publishedDate, result)

        result = newsDao.getLatestPublishedDateOfBlog(blog2.feed_url)
        Assert.assertNotNull(result)
        Assert.assertEquals(list2.first().publishedDate, result)

        result = newsDao.getLatestPublishedDateOfBlog(blog1.feed_url + blog2.feed_url)
        Assert.assertNull(result)
    }

    @Test
    fun testGetLatestNewsPublishedDate() {

        var result = newsDao.getLatestNewsPublishedDate()
        Assert.assertNull(result)

        val list = generateNewsListAndInsert(
            newsDao,
            generateBlogAndInsert(blogDao),
            generateAuthorAndInsert(authorDao)
        ).sortedBy { it.publishedDate }.reversed()

        result = newsDao.getLatestNewsPublishedDate()
        Assert.assertNotNull(result)
        Assert.assertEquals(list.first().publishedDate, result)
    }

    @Test
    fun testGetAllActiveNews() {

        val blog1 = generateBlog()
        blog1.active = true
        val blog2 = generateBlog()
        blog2.active = false
        blogDao.insert(blog1)
        blogDao.insert(blog2)

        val list1 =
            generateNewsListAndInsert(newsDao, blog1, generateAuthorAndInsert(authorDao))

        val list2 =
            generateNewsListAndInsert(newsDao, blog2, generateAuthorAndInsert(authorDao))

        var result = LiveDataTestUtil.getValue(LivePagedListBuilder(newsDao.getAllActiveNews(""), 10000).build())
        Assert.assertNotNull(result)
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(list1.size, result.size)
        Assert.assertTrue(result.containsAll(list1))
        Assert.assertFalse(result.containsAll(list2))

        result = LiveDataTestUtil.getValue(
            LivePagedListBuilder(
                newsDao.getAllActiveNews(list1.first().title!!),
                10000
            ).build()
        )
        Assert.assertNotNull(result)
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(result.size, 1)
        Assert.assertEquals(result.first(), list1.first())

        result = LiveDataTestUtil.getValue(LivePagedListBuilder(newsDao.getAllActiveNews(blog1.name), 10000).build())
        Assert.assertNotNull(result)
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(list1.size, result.size)
        Assert.assertTrue(result.containsAll(list1))
        Assert.assertFalse(result.containsAll(list2))

        result = LiveDataTestUtil.getValue(
            LivePagedListBuilder(
                newsDao.getAllActiveNews(list2.last().title!!),
                10000
            ).build()
        )
        Assert.assertNotNull(result)
        Assert.assertTrue(result.isEmpty())

        result = LiveDataTestUtil.getValue(LivePagedListBuilder(newsDao.getAllActiveNews(blog2.name), 10000).build())
        Assert.assertNotNull(result)
        Assert.assertTrue(result.isEmpty())
    }

    @Test
    fun testGetCountOfNews() {

        var result = newsDao.getCountOfNews()
        Assert.assertEquals(0, result)

        val list =
            generateNewsListAndInsert(newsDao, generateBlogAndInsert(blogDao), generateAuthorAndInsert(authorDao))

        result = newsDao.getCountOfNews()
        Assert.assertEquals(list.size, result)

        val list2 =
            generateNewsListAndInsert(newsDao, generateBlogAndInsert(blogDao), generateAuthorAndInsert(authorDao))

        result = newsDao.getCountOfNews()
        Assert.assertEquals(list.size + list2.size, result)
    }

    @Test
    fun testGetLatestNewsByDate() {

        var result = newsDao.getLatestNewsByDate(LocalDateTime.now())
        Assert.assertTrue(result.isEmpty())

        val list = generateNewsListAndInsert(
            newsDao,
            generateBlogAndInsert(blogDao),
            generateAuthorAndInsert(authorDao)
        ).sortedBy { it.publishedDate }.reversed()

        result = newsDao.getLatestNewsByDate(list.first().publishedDate!!)
        Assert.assertTrue(result.isEmpty())

        result = newsDao.getLatestNewsByDate(list[1].publishedDate!!)
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(1, result.size)
        Assert.assertTrue(result.contains(list.first()))

        result = newsDao.getLatestNewsByDate(list[2].publishedDate!!)
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(2, result.size)
        Assert.assertTrue(result.containsAll(listOf(list.first(), list[1])))

        result = newsDao.getLatestNewsByDate(list[3].publishedDate!!)
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(3, result.size)
        Assert.assertTrue(result.containsAll(listOf(list.first(), list[1], list[2])))

        result = newsDao.getLatestNewsByDate(list[4].publishedDate!!)
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(4, result.size)
        Assert.assertTrue(result.containsAll(listOf(list.first(), list[1], list[2], list[3])))
    }
}
