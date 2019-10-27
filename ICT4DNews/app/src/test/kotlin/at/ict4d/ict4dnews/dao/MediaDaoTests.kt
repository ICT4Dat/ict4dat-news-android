package at.ict4d.ict4dnews.dao

import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import at.ict4d.ict4dnews.utils.generateAuthorAndInsert
import at.ict4d.ict4dnews.utils.generateBlogAndInsert
import at.ict4d.ict4dnews.utils.generateMedia
import at.ict4d.ict4dnews.utils.generateMediaListAndInsert
import at.ict4d.ict4dnews.utils.generateNewsAndInsert
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class MediaDaoTests : BaseDaoTest() {

    private lateinit var mediaDao: MediaDao
    private lateinit var blogDao: BlogDao
    private lateinit var authorDao: AuthorDao
    private lateinit var newsDao: NewsDao

    @Before
    override fun createDatabase() {
        super.createDatabase()
        mediaDao = appDatabase.mediaDao()
        blogDao = appDatabase.blogDao()
        authorDao = appDatabase.authorDao()
        newsDao = appDatabase.newsDao()
    }

    @Test
    fun testInsert() {
        val author = generateAuthorAndInsert(authorDao)
        val result = mediaDao.insert(
            generateMedia(
                generateNewsAndInsert(newsDao, generateBlogAndInsert(blogDao), author),
                author
            )
        )
        Assert.assertEquals(1L, result)
    }

    @Test
    fun testInsertAll() {
        val list = generateMediaListAndInsert(mediaDao, authorDao, blogDao, newsDao)

        val result = mediaDao.insertAll(list)
        Assert.assertEquals(list.size, result.size)
    }

    @Test
    fun testGetAll() {
        val list = generateMediaListAndInsert(mediaDao, authorDao, blogDao, newsDao)

        mediaDao.getAll().test()
            .assertValue { it.isNotEmpty() }
            .assertValue { it.size == list.size }
            .assertValue { it.containsAll(list) }
    }
}
