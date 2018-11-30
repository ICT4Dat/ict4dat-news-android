package at.ict4d.ict4dnews.persistence.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import at.ict4d.ict4dnews.models.BLOG_TABLE_ACTIVE
import at.ict4d.ict4dnews.models.BLOG_TABLE_NAME
import at.ict4d.ict4dnews.models.BLOG_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.BLOG_TABLE_URL
import at.ict4d.ict4dnews.models.Blog
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
abstract class BlogDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(blog: Blog)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(blogs: List<Blog>)

    @Query("SELECT * FROM $BLOG_TABLE_TABLE_NAME ORDER BY $BLOG_TABLE_NAME")
    abstract fun getAll(): LiveData<List<Blog>>

    @Query("SELECT * FROM $BLOG_TABLE_TABLE_NAME WHERE $BLOG_TABLE_ACTIVE = 1 ORDER BY $BLOG_TABLE_NAME")
    abstract fun getAllActiveBlogs(): List<Blog>

    @Query("SELECT $BLOG_TABLE_URL FROM $BLOG_TABLE_TABLE_NAME WHERE $BLOG_TABLE_URL LIKE :fuzzyURL")
    abstract fun getBlogURLByFuzzyURL(fuzzyURL: String): String?

    @Query("SELECT * FROM $BLOG_TABLE_TABLE_NAME WHERE $BLOG_TABLE_URL = :feed_url")
    abstract fun getBlogByURL(feed_url: String): LiveData<Blog>

    @Update
    abstract fun updateBlog(blog: Blog)

    @Query("SELECT * FROM $BLOG_TABLE_TABLE_NAME ORDER BY $BLOG_TABLE_NAME")
    abstract fun getAllBlogsAsList(): List<Blog>

    @Query("SELECT * FROM $BLOG_TABLE_TABLE_NAME WHERE $BLOG_TABLE_ACTIVE = 1 ORDER BY $BLOG_TABLE_NAME")
    abstract fun getAllActiveBlogsAsFlowable(): Flowable<List<Blog>>

    @Query("SELECT COUNT(*) FROM $BLOG_TABLE_TABLE_NAME")
    protected abstract fun getBlogsCount(): Int

    fun isBlogsExist(): Boolean = getBlogsCount() >= 1

    @Query("SELECT COUNT(*) FROM $BLOG_TABLE_TABLE_NAME WHERE $BLOG_TABLE_ACTIVE = 1")
    abstract fun getActiveBlogsCount(): Single<Int>
}