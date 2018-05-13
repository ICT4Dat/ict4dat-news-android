package at.ict4d.ict4dnews.persistence.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor

@Dao
abstract class WordpressAuthorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(author: WordpressAuthor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(authors: List<WordpressAuthor>)

    @Query("SELECT * FROM ${WordpressAuthor.TABLE_TABLE_NAME}")
    abstract fun getAll(): LiveData<List<WordpressAuthor>>

}