package at.ict4d.ict4dnews.utils

import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao

fun generateAuthor() = Author(
    generateRandomURL(),
    generateRandomNumber(),
    generateUUID(),
    generateRandomURL(),
    generateUUID(),
    generateUUID()
)

fun generateAuthorAndInsert(authorDao: AuthorDao): Author {
    val author = generateAuthor()
    authorDao.insert(author)
    return author
}

fun generateAuthorListAndInsert(authorDao: AuthorDao, numberOfAuthors: Int = 5): List<Author> {
    val authorList = mutableListOf<Author>()
    for (i in 0 until numberOfAuthors) {
        authorList.add(generateAuthor())
    }

    authorDao.insertAll(authorList)
    return authorList
}
