package at.ict4d.ict4dnews.persistence.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.ict4d.ict4dnews.models.BLOG_TABLE_ACTIVE
import at.ict4d.ict4dnews.models.BLOG_TABLE_FEED_URL
import at.ict4d.ict4dnews.models.BLOG_TABLE_NAME
import at.ict4d.ict4dnews.models.BLOG_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.NEWS_TABLE_BLOG_ID
import at.ict4d.ict4dnews.models.NEWS_TABLE_PUBLISHED_DATE
import at.ict4d.ict4dnews.models.NEWS_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.NEWS_TABLE_TITLE
import at.ict4d.ict4dnews.models.News
import io.reactivex.Flowable
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
    abstract fun getLatestBlogPublishedDateOfBlog(blogID: String): LocalDateTime?

    @Query("SELECT $NEWS_TABLE_PUBLISHED_DATE FROM $NEWS_TABLE_TABLE_NAME ORDER BY datetime($NEWS_TABLE_PUBLISHED_DATE) DESC LIMIT 1")
    abstract fun getLatestNewsPublishedDate(): LocalDateTime?

    @Query("SELECT * FROM $NEWS_TABLE_TABLE_NAME INNER JOIN $BLOG_TABLE_TABLE_NAME ON $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_BLOG_ID = $BLOG_TABLE_TABLE_NAME.$BLOG_TABLE_FEED_URL WHERE $BLOG_TABLE_TABLE_NAME.$BLOG_TABLE_ACTIVE = 1 AND ($NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_TITLE LIKE '%' || :query || '%' OR $BLOG_TABLE_TABLE_NAME.$BLOG_TABLE_NAME LIKE '%' || :query || '%') ORDER BY datetime($NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_PUBLISHED_DATE) DESC")
    abstract fun getAllActiveNews(query: String): DataSource.Factory<Int, News>

    @Query("SELECT * FROM $NEWS_TABLE_TABLE_NAME INNER JOIN $BLOG_TABLE_TABLE_NAME ON $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_BLOG_ID = $BLOG_TABLE_TABLE_NAME.$BLOG_TABLE_FEED_URL WHERE $BLOG_TABLE_TABLE_NAME.$BLOG_TABLE_ACTIVE = 1 ORDER BY datetime($NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_PUBLISHED_DATE) DESC")
    abstract fun getAllActiveNewsAsFlowable(): Flowable<List<News>>

    @Query("SELECT * FROM $NEWS_TABLE_TABLE_NAME INNER JOIN $BLOG_TABLE_TABLE_NAME ON $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_BLOG_ID = $BLOG_TABLE_TABLE_NAME.$BLOG_TABLE_FEED_URL WHERE $BLOG_TABLE_TABLE_NAME.$BLOG_TABLE_ACTIVE = 1 ORDER BY datetime($NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_PUBLISHED_DATE) DESC")
    abstract fun getAllActiveNewsAsList(): List<News>

    @Query("SELECT COUNT() FROM $NEWS_TABLE_TABLE_NAME")
    abstract fun getCountOfNews(): Int

    @Query("SELECT * FROM $NEWS_TABLE_TABLE_NAME WHERE datetime($NEWS_TABLE_PUBLISHED_DATE) > :recentNewsDate")
    abstract fun getLatestNewsByDate(recentNewsDate: LocalDateTime): List<News>
}