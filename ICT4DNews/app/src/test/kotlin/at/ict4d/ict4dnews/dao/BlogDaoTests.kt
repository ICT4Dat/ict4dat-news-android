package at.ict4d.ict4dnews.dao

import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.utils.generateBlog
import at.ict4d.ict4dnews.utils.generateBlogAndInsert
import at.ict4d.ict4dnews.utils.generateListBlogAndInsert
import at.ict4d.ict4dnews.utils.generateRandomURL
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class BlogDaoTests : BaseDaoTest() {

    private lateinit var blogDao: BlogDao

    @Before
    override fun createDatabase() {
        super.createDatabase()
        blogDao = appDatabase.blogDao()
    }

    @Test
    fun testInsert() {
        val result = blogDao.insert(generateBlog())
        Assert.assertEquals(1L, result)
    }

    @Test
    fun testInsertAll() {
        val list = generateListBlogAndInsert(blogDao)
        val result = blogDao.insertAll(list)
        Assert.assertEquals(list.size, result.size)
    }

    @Test
    fun testGetAll() = runBlocking {
        var result = blogDao.getAllBlogs().first()
        Assert.assertTrue(result.isEmpty())

        val list = generateListBlogAndInsert(blogDao)

        result = blogDao.getAllBlogs().first()
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(list.size, result.size)
        Assert.assertTrue(result.containsAll(list))
    }

    @Test
    fun testGetAllActiveBlogs() = runBlocking {

        var result = blogDao.getAllActiveBlogs().first()
        Assert.assertTrue(result.isEmpty())

        val blog1 = generateBlogAndInsert(blogDao, active = true)
        val blog2 = generateBlogAndInsert(blogDao, active = true)
        generateBlogAndInsert(blogDao, active = false)
        val blog4 = generateBlogAndInsert(blogDao, active = true)

        result = blogDao.getAllActiveBlogs().first()
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(3, result.size)
        Assert.assertTrue(result.containsAll(listOf(blog1, blog2, blog4)))
    }

    @Test
    fun testGetBlogByUrl() = runBlocking {

        var result = blogDao.getBlogByUrl(generateRandomURL()).first()
        Assert.assertNull(result)

        val list = generateListBlogAndInsert(blogDao).shuffled()

        result = blogDao.getBlogByUrl(list.first().feed_url).first()
        Assert.assertNotNull(result)
        Assert.assertEquals(list.first(), result)

        result = blogDao.getBlogByUrl(list.last().feed_url).first()
        Assert.assertNotNull(result)
        Assert.assertEquals(list.last(), result)

        result = blogDao.getBlogByUrl(list.joinToString { it.feed_url }).first()
        Assert.assertNull(result)
    }

    @Test
    fun testUpdateBlog() = runBlocking {
        val blog = generateBlogAndInsert(blogDao, active = true)
        var result = blogDao.getBlogByUrl(blog.feed_url).first()
        Assert.assertEquals(blog, result)

        blog.active = false
        val index = blogDao.updateBlog(blog)
        Assert.assertEquals(1, index)

        result = blogDao.getBlogByUrl(blog.feed_url).first()
        Assert.assertEquals(blog, result)
    }

    @Test
    fun testIsBlogsExist() = runBlocking {
        var result = blogDao.doBlogsExist().first()
        Assert.assertFalse(result)

        generateListBlogAndInsert(blogDao)

        result = blogDao.doBlogsExist().first()
        Assert.assertTrue(result)
    }
}
