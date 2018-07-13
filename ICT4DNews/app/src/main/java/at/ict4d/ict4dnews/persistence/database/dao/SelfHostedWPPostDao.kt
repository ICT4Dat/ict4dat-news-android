package at.ict4d.ict4dnews.persistence.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import io.reactivex.Flowable

@Dao
abstract class SelfHostedWPPostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(post: SelfHostedWPPost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(posts: List<SelfHostedWPPost>)

    @Query("SELECT * FROM ${SelfHostedWPPost.TABLE_TABLE_NAME}")
    abstract fun getAll(): Flowable<List<SelfHostedWPPost>>

}