package at.ict4d.ict4dnews.persistence.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters
import at.ict4d.ict4dnews.ICT4DNewsApplication
import at.ict4d.ict4dnews.models.AuthorModel
import at.ict4d.ict4dnews.models.MediaModel
import at.ict4d.ict4dnews.models.NewsModel
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import at.ict4d.ict4dnews.persistence.database.dao.NewsDao
import at.ict4d.ict4dnews.persistence.database.dao.AuthorDao
import at.ict4d.ict4dnews.persistence.database.dao.MediaDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Collections.emptyList
import javax.inject.Inject


@Database(entities = [NewsModel::class, AuthorModel::class, MediaModel::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun newsDao(): NewsDao

    abstract fun authorDao(): AuthorDao

    abstract fun mediaDao(): MediaDao

    companion object {
        const val DATABASE_NAME = "ict4d_news_database"
    }
}

class Converters {

    @Inject
    lateinit var gson: Gson

    init {
        ICT4DNewsApplication.component.inject(this)
    }

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
    fun mediaTypeFromString(string: String): MediaType? {
        return MediaType.parse(string)
    }

    @TypeConverter
    fun stringFromMediaType(mediaType: MediaType): String {
        return mediaType.toString()
    }
}