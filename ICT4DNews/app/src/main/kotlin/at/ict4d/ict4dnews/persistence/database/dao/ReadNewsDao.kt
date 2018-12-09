package at.ict4d.ict4dnews.persistence.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.ict4d.ict4dnews.models.READ_NEWS_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.ReadNews

@Dao
abstract class ReadNewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(readNews: ReadNews)

    @Query("SELECT * FROM $READ_NEWS_TABLE_TABLE_NAME")
    abstract fun getAll(): LiveData<List<ReadNews>>

    @Query("DELETE FROM $READ_NEWS_TABLE_TABLE_NAME")
    abstract fun deleteAll()
}