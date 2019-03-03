package at.ict4d.ict4dnews.utils

import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao

fun generateAuthorName() = "Author ${generateRandomNumber()}"

fun generateAuthorUserName() = "User Name ${generateRandomNumber()}"

fun generateAuthor() = Author(generateRandomURL(), generateRandomNumber(), generateAuthorName(), generateRandomURL(), generateDescription(), generateAuthorUserName())

fun generateAuthorAndInsert(authorDao: AuthorDao): Author {
    val author = generateAuthor()
    authorDao.insert(author)
    return author
}