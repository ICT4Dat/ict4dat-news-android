package at.ict4d.ict4dnews.persistence.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.ict4d.ict4dnews.models.MEDIA_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.Media

@Dao
abstract class MediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(media: Media)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(media: List<Media>)

    @Query("SELECT * FROM $MEDIA_TABLE_TABLE_NAME")
    abstract fun getAll(): LiveData<List<Media>>
}