package at.ict4d.ict4dnews.persistence.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import at.ict4d.ict4dnews.models.BLOG_TABLE_ACTIVE
import at.ict4d.ict4dnews.models.BLOG_TABLE_FEED_URL
import at.ict4d.ict4dnews.models.BLOG_TABLE_NAME
import at.ict4d.ict4dnews.models.BLOG_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.Blog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Dao
abstract class BlogDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(blog: Blog): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(blogs: List<Blog>): List<Long>

    @Query(
        """
       SELECT * 
       FROM $BLOG_TABLE_TABLE_NAME 
       ORDER BY $BLOG_TABLE_NAME 
    """,
    )
    abstract fun getAllBlogs(): Flow<List<Blog>>

    @Query(
        """
       SELECT * 
       FROM $BLOG_TABLE_TABLE_NAME 
       WHERE $BLOG_TABLE_ACTIVE = 1 
       ORDER BY $BLOG_TABLE_NAME
    """,
    )
    abstract fun getAllActiveBlogs(): Flow<List<Blog>>

    @Query(
        """
        SELECT * 
        FROM $BLOG_TABLE_TABLE_NAME 
        WHERE $BLOG_TABLE_FEED_URL = :feedUrl
        """,
    )
    abstract fun getBlogByUrl(feedUrl: String): Flow<Blog?>

    @Update
    abstract suspend fun updateBlog(blog: Blog): Int

    @Query(
        """
        SELECT COUNT(*) 
        FROM $BLOG_TABLE_TABLE_NAME
        """,
    )
    abstract fun getBlogsCount(): Flow<Int>

    fun doBlogsExist() = getBlogsCount().map { it > 0 }

    @Query(
        """
        SELECT COUNT(*) 
        FROM $BLOG_TABLE_TABLE_NAME 
        WHERE $BLOG_TABLE_ACTIVE = 1
        """,
    )
    abstract fun getActiveBlogsCount(): Flow<Int>
}
