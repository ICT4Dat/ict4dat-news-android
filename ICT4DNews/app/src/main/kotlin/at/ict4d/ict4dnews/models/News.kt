package at.ict4d.ict4dnews.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import at.ict4d.ict4dnews.extensions.stripHtml
import at.ict4d.ict4dnews.models.wordpress.SELF_HOSTED_WP_POST_SERIALIZED_RENDERED
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

const val NEWS_TABLE_TABLE_NAME = "news"
const val NEWS_TABLE_LINK = "link"
const val NEWS_TABLE_AUTHOR_ID = "author_id"
const val NEWS_TABLE_TITLE = "title"
const val NEWS_TABLE_FEATURED_MEDIA = "featured_media"
const val NEWS_TABLE_SERVER_ID = "server_id"
const val NEWS_TABLE_PUBLISHED_DATE = "published_date"
const val NEWS_TABLE_BLOG_ID = "blog_id"

@Parcelize
@Entity(
    tableName = NEWS_TABLE_TABLE_NAME,

    foreignKeys = [
        ForeignKey(
            entity = Author::class,
            parentColumns = [AUTHOR_TABLE_LINK],
            childColumns = [NEWS_TABLE_AUTHOR_ID],
        ),
        ForeignKey(
            entity = Blog::class,
            parentColumns = [BLOG_TABLE_FEED_URL],
            childColumns = [NEWS_TABLE_BLOG_ID],
        ),
    ],

    indices = [
        Index(value = [NEWS_TABLE_AUTHOR_ID]),
        Index(value = [NEWS_TABLE_BLOG_ID]),
    ],
)
data class News(

    @PrimaryKey
    @ColumnInfo(name = NEWS_TABLE_LINK)
    val link: String,

    @ColumnInfo(name = NEWS_TABLE_AUTHOR_ID)
    val authorID: String?,

    @ColumnInfo(name = NEWS_TABLE_SERVER_ID)
    val serverID: Int,

    @ColumnInfo(name = NEWS_TABLE_FEATURED_MEDIA)
    val mediaFeaturedURL: String? = null,

    @ColumnInfo(name = NEWS_TABLE_TITLE)
    var title: String? = null,

    @ColumnInfo(name = NEWS_TABLE_PUBLISHED_DATE)
    val publishedDate: LocalDateTime? = null,

    @ColumnInfo(name = NEWS_TABLE_BLOG_ID)
    val blogID: String?,
) : Parcelable {
    constructor(selfHostedWPPost: SelfHostedWPPost) : this(
        selfHostedWPPost.link,
        selfHostedWPPost.authorLink,
        selfHostedWPPost.serverID,
        selfHostedWPPost.featuredMediaLink,
        selfHostedWPPost.title[SELF_HOSTED_WP_POST_SERIALIZED_RENDERED]?.stripHtml(),
        selfHostedWPPost.date,
        selfHostedWPPost.blogLink,
    )
}
