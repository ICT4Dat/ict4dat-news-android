package at.ict4d.ict4dnews.persistence.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.ict4d.ict4dnews.models.AUTHOR_TABLE_LINK
import at.ict4d.ict4dnews.models.AUTHOR_TABLE_TABLE_NAME

import at.ict4d.ict4dnews.models.Author

@Dao
abstract class AuthorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(author: Author)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(authors: List<Author>)

    @Query("SELECT * FROM $AUTHOR_TABLE_TABLE_NAME")
    abstract fun getAll(): LiveData<List<Author>>

    @Query("SELECT * FROM $AUTHOR_TABLE_TABLE_NAME WHERE $AUTHOR_TABLE_LINK = :authorId")
    abstract fun getAuthorDetailsBy(authorId: String): LiveData<Author>
}