package at.ict4d.ict4dnews.dao

import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.utils.generateAuthor
import at.ict4d.ict4dnews.utils.generateAuthorListAndInsert
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
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
    fun testGetAuthorDetailsBy() =
        runBlocking {
            val list = generateAuthorListAndInsert(authorDao)

            var result = authorDao.getAuthorDetailsBy(list.first().link).first()
            Assert.assertNotNull(result)
            Assert.assertEquals(list.first(), result)

            result = authorDao.getAuthorDetailsBy(list.last().link).first()
            Assert.assertNotNull(result)
            Assert.assertEquals(list.last(), result)

            result = authorDao.getAuthorDetailsBy(list.joinToString { it.link }).first()
            Assert.assertNull(result)
        }
}
