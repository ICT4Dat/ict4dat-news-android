package at.ict4d.ict4dnews.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

const val BLOG_TABLE_TABLE_NAME = "blogs"
const val BLOG_TABLE_FEED_URL = "blog_feed_url"
const val BLOG_TABLE_URL = "blog_url"
const val BLOG_TABLE_NAME = "blog_name"
const val BLOG_TABLE_DESCRIPTION = "blog_description"
const val BLOG_TABLE_FEED_TYPE = "blog_feed_type"
const val BLOG_TABLE_LOGO_URL = "blog_logo_url"
const val BLOG_TABLE_ACTIVE = "blog_active"

const val BLOG_SERIALIZED_FEED_URL = "feed_url"
const val BLOG_SERIALIZED_URL = "url"
const val BLOG_SERIALIZED_NAME = "name"
const val BLOG_SERIALIZED_DESCRIPTION = "description"
const val BLOG_SERIALIZED_FEED_TYPE = "feed_type"
const val BLOG_SERIALIZED_LOGO_URL = "logo_url"

@Parcelize
@Entity(tableName = BLOG_TABLE_TABLE_NAME)
data class Blog(

    @PrimaryKey
    @ColumnInfo(name = BLOG_TABLE_FEED_URL)
    @SerializedName(BLOG_SERIALIZED_FEED_URL)
    val feed_url: String,

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
    var logoURL: String?,

    @ColumnInfo(name = BLOG_TABLE_ACTIVE)
    var active: Boolean = true
) : Parcelable

enum class FeedType {
    SELF_HOSTED_WP_BLOG,
    WORDPRESS_COM,
    RSS
}
