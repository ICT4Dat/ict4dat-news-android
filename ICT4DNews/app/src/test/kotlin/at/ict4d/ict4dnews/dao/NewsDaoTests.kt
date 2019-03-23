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

        result = LiveDataTestUtil.getValue(LivePagedListBuilder(newsDao.getAllActiveNews(list1.first().title!!), 10000).build())
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

        result = LiveDataTestUtil.getValue(LivePagedListBuilder(newsDao.getAllActiveNews(list2.last().title!!), 10000).build())
        Assert.assertNotNull(result)
        Assert.assertTrue(result.isEmpty())

        result = LiveDataTestUtil.getValue(LivePagedListBuilder(newsDao.getAllActiveNews(blog2.name), 10000).build())
        Assert.assertNotNull(result)
        Assert.assertTrue(result.isEmpty())
    }
}