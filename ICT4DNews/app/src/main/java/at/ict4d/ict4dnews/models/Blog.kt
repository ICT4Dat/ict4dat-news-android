package at.ict4d.ict4dnews.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

const val BLOG_TABLE_TABLE_NAME = "blogs"
const val BLOG_TABLE_URL = "url"
const val BLOG_TABLE_NAME = "name"
const val BLOG_TABLE_DESCRIPTION = "description"
const val BLOG_TABLE_FEED_TYPE = "feed_type"
const val BLOG_TABLE_LOGO_URL = "logo_url"

const val BLOG_SERIALIZED_URL = "url"
const val BLOG_SERIALIZED_NAME = "name"
const val BLOG_SERIALIZED_DESCRIPTION = "description"
const val BLOG_SERIALIZED_FEED_TYPE = "feed_type"
const val BLOG_SERIALIZED_LOGO_URL = "logo_url"

@Entity(tableName = BLOG_TABLE_TABLE_NAME)
data class Blog(

    @PrimaryKey
    @ColumnInfo(name = BLOG_TABLE_URL)
    @SerializedName(BLOG_SERIALIZED_URL)
    val url: String,

    @ColumnInfo(name = BLOG_TABLE_NAME)
    @SerializedName(BLOG_SERIALIZED_NAME)
    val name: String,

    @ColumnInfo(name = BLOG_TABLE_DESCRIPTION)
    @SerializedName(BLOG_SERIALIZED_DESCRIPTION)
    val description: String,

    @ColumnInfo(name = BLOG_TABLE_FEED_TYPE)
    @SerializedName(BLOG_SERIALIZED_FEED_TYPE)
    val feedType: FeedType,

    @ColumnInfo(name = BLOG_TABLE_LOGO_URL)
    @SerializedName(BLOG_SERIALIZED_LOGO_URL)
    val logoURL: String?
)

enum class FeedType {
    SELF_HOSTED_WP_BLOG, WORDPRESS_COM, RSS
}