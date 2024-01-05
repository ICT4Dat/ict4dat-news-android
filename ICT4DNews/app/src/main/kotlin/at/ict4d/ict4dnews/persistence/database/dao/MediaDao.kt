package at.ict4d.ict4dnews.persistence.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import at.ict4d.ict4dnews.models.Media

@Dao
abstract class MediaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(media: Media): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(media: List<Media>): List<Long>
}
