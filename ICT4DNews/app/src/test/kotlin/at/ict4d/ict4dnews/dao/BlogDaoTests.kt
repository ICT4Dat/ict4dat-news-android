package at.ict4d.ict4dnews.dao

import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.utils.LiveDataTestUtil
import at.ict4d.ict4dnews.utils.generateBlog
import at.ict4d.ict4dnews.utils.generateBlogAndInsert
import at.ict4d.ict4dnews.utils.generateListBlogAndInsert
import at.ict4d.ict4dnews.utils.generateRandomURL
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
    fun testGetAll() {
        var result = LiveDataTestUtil.getValue(blogDao.getAll())
        Assert.assertTrue(result.isEmpty())

        val list = generateListBlogAndInsert(blogDao)

        result = LiveDataTestUtil.getValue(blogDao.getAll())
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(list.size, result.size)
        Assert.assertTrue(result.containsAll(list))
    }

    @Test
    fun testGetAllActiveBlogs() {

        var result = blogDao.getAllActiveBlogs()
        Assert.assertTrue(result.isEmpty())

        val blog1 = generateBlogAndInsert(blogDao, active = true)
        val blog2 = generateBlogAndInsert(blogDao, active = true)
        generateBlogAndInsert(blogDao, active = false)
        val blog4 = generateBlogAndInsert(blogDao, active = true)

        result = blogDao.getAllActiveBlogs()
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(3, result.size)
        Assert.assertTrue(result.containsAll(listOf(blog1, blog2, blog4)))
    }

    @Test
    fun testGetBlogByUrlAsLiveData() {

        var result = LiveDataTestUtil.getValue(blogDao.getBlogByUrlAsLiveData(generateRandomURL()))
        Assert.assertNull(result)

        val list = generateListBlogAndInsert(blogDao).shuffled()

        result = LiveDataTestUtil.getValue(blogDao.getBlogByUrlAsLiveData(list.first().feed_url))
        Assert.assertNotNull(result)
        Assert.assertEquals(list.first(), result)

        result = LiveDataTestUtil.getValue(blogDao.getBlogByUrlAsLiveData(list.last().feed_url))
        Assert.assertNotNull(result)
        Assert.assertEquals(list.last(), result)

        result = LiveDataTestUtil.getValue(blogDao.getBlogByUrlAsLiveData(list.joinToString { it.feed_url }))
        Assert.assertNull(result)
    }

    @Test
    fun testGetBlogByUrl() {

        var result = blogDao.getBlogByUrl(generateRandomURL())
        Assert.assertNull(result)

        val list = generateListBlogAndInsert(blogDao).shuffled()

        result = blogDao.getBlogByUrl(list.first().feed_url)
        Assert.assertNotNull(result)
        Assert.assertEquals(list.first(), result)

        result = blogDao.getBlogByUrl(list.last().feed_url)
        Assert.assertNotNull(result)
        Assert.assertEquals(list.last(), result)

        result = blogDao.getBlogByUrl(list.joinToString { it.feed_url })
        Assert.assertNull(result)
    }

    @Test
    fun testUpdateBlog() {
        val blog = generateBlogAndInsert(blogDao, active = true)
        var result = blogDao.getBlogByUrl(blog.feed_url)
        Assert.assertEquals(blog, result)

        blog.active = false
        val index = blogDao.updateBlog(blog)
        Assert.assertEquals(1, index)

        result = blogDao.getBlogByUrl(blog.feed_url)
        Assert.assertEquals(blog, result)
    }

    @Test
    fun testGetAllBlogsAsList() {
        var result = blogDao.getAllBlogsAsList()
        Assert.assertTrue(result.isEmpty())

        val list = generateListBlogAndInsert(blogDao)

        result = blogDao.getAllBlogsAsList()
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(list.size, result.size)
        Assert.assertTrue(result.containsAll(list))
    }

    @Test
    fun testIsBlogsExist() {
        var result = blogDao.isBlogsExist()
        Assert.assertFalse(result)

        generateListBlogAndInsert(blogDao)

        result = blogDao.isBlogsExist()
        Assert.assertTrue(result)
    }

    @Test
    fun testGetBlogsCountAsLiveData() {
        var result = LiveDataTestUtil.getValue(blogDao.getBlogsCountAsLiveData())
        Assert.assertEquals(0, result)

        val list = generateListBlogAndInsert(blogDao)

        result = LiveDataTestUtil.getValue(blogDao.getBlogsCountAsLiveData())
        Assert.assertEquals(list.size, result)
    }

    @Test
    fun testGetActiveBlogsCountAsLiveData() {
        var result = LiveDataTestUtil.getValue(blogDao.getActiveBlogsCountAsLiveData())
        Assert.assertEquals(0, result)

        generateBlogAndInsert(blogDao, active = true)
        generateBlogAndInsert(blogDao, active = true)
        generateBlogAndInsert(blogDao, active = false)
        generateBlogAndInsert(blogDao, active = true)

        result = LiveDataTestUtil.getValue(blogDao.getActiveBlogsCountAsLiveData())
        Assert.assertEquals(3, result)
    }
}
