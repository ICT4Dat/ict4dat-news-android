package at.ict4d.ict4dnews.persistence.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import io.reactivex.Flowable

@Dao
abstract class WordpressMediaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(media: WordpressMedia)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(media: List<WordpressMedia>)

    @Query("SELECT * FROM ${WordpressMedia.TABLE_TABLE_NAME}")
    abstract fun getAll(): Flowable<List<WordpressMedia>>

}