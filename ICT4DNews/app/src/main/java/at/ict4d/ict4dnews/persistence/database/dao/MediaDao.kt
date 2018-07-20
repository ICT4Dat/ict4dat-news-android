package at.ict4d.ict4dnews.persistence.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import at.ict4d.ict4dnews.models.MEDIA_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.MediaModel
import io.reactivex.Flowable

@Dao
abstract class MediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(media: MediaModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(media: List<MediaModel>)

    @Query("SELECT * FROM $MEDIA_TABLE_TABLE_NAME")
    abstract fun getAll(): LiveData<List<MediaModel>>

}