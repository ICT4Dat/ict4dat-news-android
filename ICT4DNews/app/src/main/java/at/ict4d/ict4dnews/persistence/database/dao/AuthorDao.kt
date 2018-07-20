package at.ict4d.ict4dnews.persistence.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import at.ict4d.ict4dnews.models.AUTHOR_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.AuthorModel
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import io.reactivex.Flowable

@Dao
abstract class AuthorDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(author: AuthorModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(authors: List<AuthorModel>)

    @Query("SELECT * FROM $AUTHOR_TABLE_TABLE_NAME")
    abstract fun getAll(): LiveData<List<AuthorModel>>

}