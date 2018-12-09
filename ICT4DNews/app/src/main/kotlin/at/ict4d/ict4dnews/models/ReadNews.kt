package at.ict4d.ict4dnews.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

const val READ_NEWS_TABLE_TABLE_NAME = "read_news"
const val READ_NEWS_TABLE_LINK = "news_url"

@Entity(tableName = READ_NEWS_TABLE_TABLE_NAME)
data class ReadNews(

    @PrimaryKey
    @ColumnInfo(name = READ_NEWS_TABLE_LINK)
    val newsUrl: String
)