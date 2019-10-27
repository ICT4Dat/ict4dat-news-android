package at.ict4d.ict4dnews.persistence.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import at.ict4d.ict4dnews.models.Author
import at.ict4d.ict4dnews.models.Blog
import at.ict4d.ict4dnews.models.FeedType
import at.ict4d.ict4dnews.models.Media
import at.ict4d.ict4dnews.models.News
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.BlogDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections.emptyList
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@Database(entities = [News::class, Author::class, Media::class, Blog::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    abstract fun authorDao(): AuthorDao

    abstract fun mediaDao(): MediaDao

    abstract fun blogDao(): BlogDao

    companion object {
        const val DATABASE_NAME = "ict4d_news_database"
    }
}

class Converters : KoinComponent {

    private val gson: Gson by inject()

    @TypeConverter
    fun localDateTimeFromString(value: String?): LocalDateTime? {
        return if (value == null) null else LocalDateTime.parse(value)
    }

    @TypeConverter
    fun localDateTimeToString(date: LocalDateTime?): String? {
        return date?.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

    @TypeConverter
    fun mapFromString(string: String): Map<String, String> {
        val listType = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(string, listType)
    }

    @TypeConverter
    fun stringFromMap(map: Map<String, String>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun stringToIntList(data: String?): List<Int> {
        if (data == null) {
            return emptyList()
        }

        val listType = object : TypeToken<List<Int>>() {}.type

        return gson.fromJson(data, listType)
    }

    @TypeConverter
    fun intListToString(someObjects: List<Int>): String {
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun feedTypeFromInt(intFeedType: Int): FeedType? {
        if (intFeedType in 0..2) {
            return FeedType.values()[intFeedType]
        }
        return null
    }

    @TypeConverter
    fun intFromFeedType(feedType: FeedType): Int {
        return feedType.ordinal
    }
}
