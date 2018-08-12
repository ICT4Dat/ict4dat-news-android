package at.ict4d.ict4dnews.persistence.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import at.ict4d.ict4dnews.models.BLOG_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.Blog

@Dao
abstract class BlogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(blog: Blog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(blogs: List<Blog>)

    @Query("SELECT * FROM $BLOG_TABLE_TABLE_NAME")
    abstract fun getAll(): LiveData<List<Blog>>
}