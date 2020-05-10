package at.ict4d.ict4dnews.persistence.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.ict4d.ict4dnews.models.BLOG_TABLE_ACTIVE
import at.ict4d.ict4dnews.models.BLOG_TABLE_FEED_URL
import at.ict4d.ict4dnews.models.BLOG_TABLE_NAME
import at.ict4d.ict4dnews.models.BLOG_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.NEWS_TABLE_AUTHOR_ID
import at.ict4d.ict4dnews.models.NEWS_TABLE_BLOG_ID
import at.ict4d.ict4dnews.models.NEWS_TABLE_DESCRIPTION
import at.ict4d.ict4dnews.models.NEWS_TABLE_FEATURED_MEDIA
import at.ict4d.ict4dnews.models.NEWS_TABLE_LINK
import at.ict4d.ict4dnews.models.NEWS_TABLE_PUBLISHED_DATE
import at.ict4d.ict4dnews.models.NEWS_TABLE_SERVER_ID
import at.ict4d.ict4dnews.models.NEWS_TABLE_TABLE_NAME
import at.ict4d.ict4dnews.models.NEWS_TABLE_TITLE
import at.ict4d.ict4dnews.models.News
import kotlinx.coroutines.flow.Flow
import org.threeten.bp.LocalDateTime

@Dao
abstract class NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(news: News): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(news: List<News>): List<Long>

    @Query(
        """
        SELECT $NEWS_TABLE_PUBLISHED_DATE 
        FROM $NEWS_TABLE_TABLE_NAME WHERE $NEWS_TABLE_BLOG_ID = :blogID 
        ORDER BY datetime($NEWS_TABLE_PUBLISHED_DATE) DESC LIMIT 1
        """
    )
    abstract fun getLatestPublishedDateOfBlog(blogID: String): Flow<LocalDateTime?>

    @Query(
        """
        SELECT $NEWS_TABLE_PUBLISHED_DATE 
        FROM $NEWS_TABLE_TABLE_NAME 
        ORDER BY datetime($NEWS_TABLE_PUBLISHED_DATE) DESC LIMIT 1"""
    )
    abstract fun getLatestNewsPublishedDate(): Flow<LocalDateTime?>

    @Query(
        """
        SELECT $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_LINK, $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_AUTHOR_ID, $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_SERVER_ID, $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_FEATURED_MEDIA, $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_TITLE, $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_DESCRIPTION, $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_PUBLISHED_DATE, $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_BLOG_ID
        FROM $NEWS_TABLE_TABLE_NAME INNER JOIN $BLOG_TABLE_TABLE_NAME ON $NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_BLOG_ID = $BLOG_TABLE_TABLE_NAME.$BLOG_TABLE_FEED_URL 
        WHERE $BLOG_TABLE_TABLE_NAME.$BLOG_TABLE_ACTIVE = 1 AND ($NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_TITLE LIKE '%' || :query || '%' OR $BLOG_TABLE_TABLE_NAME.$BLOG_TABLE_NAME LIKE '%' || :query || '%') 
        ORDER BY datetime($NEWS_TABLE_TABLE_NAME.$NEWS_TABLE_PUBLISHED_DATE) DESC
        """
    )
    abstract fun getAllActiveNews(query: String): Flow<List<News>>

    @Query(
        """
        SELECT COUNT() 
        FROM $NEWS_TABLE_TABLE_NAME
        """
    )
    abstract fun getCountOfNews(): Flow<Int>

    @Query(
        """
        SELECT * 
        FROM $NEWS_TABLE_TABLE_NAME 
        WHERE datetime($NEWS_TABLE_PUBLISHED_DATE) > :recentNewsDate
        """
    )
    abstract fun getLatestNewsByDate(recentNewsDate: LocalDateTime): Flow<List<News>>
}
