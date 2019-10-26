package at.ict4d.ict4dnews.dao

import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.utils.LiveDataTestUtil
import at.ict4d.ict4dnews.utils.generateAuthor
import at.ict4d.ict4dnews.utils.generateAuthorListAndInsert
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AuthorDaoTests : BaseDaoTest() {

    private lateinit var authorDao: AuthorDao

    @Before
    override fun createDatabase() {
        super.createDatabase()
        authorDao = appDatabase.authorDao()
    }

    @Test
    fun testInsert() {
        val result = authorDao.insert(generateAuthor())
        Assert.assertEquals(1L, result)
    }

    @Test
    fun testInsertAll() {
        val list = generateAuthorListAndInsert(authorDao)

        val result = authorDao.insertAll(list)
        Assert.assertEquals(list.size, result.size)
    }

    @Test
    fun testGetAll() {
        val list = generateAuthorListAndInsert(authorDao)

        val result = LiveDataTestUtil.getValue(authorDao.getAll())
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertEquals(list.size, result.size)
        Assert.assertTrue(result.containsAll(list))
    }

    @Test
    fun testGetAuthorDetailsBy() {
        val list = generateAuthorListAndInsert(authorDao)

        var result = LiveDataTestUtil.getValue(authorDao.getAuthorDetailsBy(list.first().link))
        Assert.assertNotNull(result)
        Assert.assertEquals(list.first(), result)

        result = LiveDataTestUtil.getValue(authorDao.getAuthorDetailsBy(list.last().link))
        Assert.assertNotNull(result)
        Assert.assertEquals(list.last(), result)

        result = LiveDataTestUtil.getValue(authorDao.getAuthorDetailsBy(list.joinToString { it.link }))
        Assert.assertNull(result)
    }
}
