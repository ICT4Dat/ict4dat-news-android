package at.ict4d.ict4dnews.persistence.database.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import at.ict4d.ict4dnews.models.NEWS_TABLE_BLOG_ID
import at.ict4d.ict4dnews.models.NEWS_TABLE_PUBLISHED_DATE
import at.ict4d.ict4dnews.models.NEWS_TABLE_TABLE_NAME

import at.ict4d.ict4dnews.models.News
import org.threeten.bp.LocalDateTime

@Dao
abstract class NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(news: News)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(news: List<News>)

    @Query("SELECT * FROM $NEWS_TABLE_TABLE_NAME ORDER BY datetime($NEWS_TABLE_PUBLISHED_DATE) DESC")
    abstract fun getAllOrderedByPublishedDate(): LiveData<List<News>>

    @Query("SELECT $NEWS_TABLE_PUBLISHED_DATE FROM $NEWS_TABLE_TABLE_NAME WHERE $NEWS_TABLE_BLOG_ID = :blogID ORDER BY datetime($NEWS_TABLE_PUBLISHED_DATE) DESC LIMIT 1")
    abstract fun getLatestBlogPublishedDate(blogID: String): LocalDateTime?
}