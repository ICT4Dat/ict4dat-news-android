package at.ict4d.ict4dnews.server.repositories

import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class AuthorRepository(private val authorDao: AuthorDao) {

    fun getAuthorBy(authorId: String) = authorDao.getAuthorDetailsBy(authorId).flowOn(Dispatchers.IO)
}
