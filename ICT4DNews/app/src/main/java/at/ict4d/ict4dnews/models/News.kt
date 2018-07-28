package at.ict4d.ict4dnews.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import at.ict4d.ict4dnews.models.wordpress.SELF_HOSTED_WP_POST_SERIALIZED_RENDERED
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import kotlinx.android.parcel.Parcelize
import org.threeten.bp.LocalDateTime

const val NEWS_TABLE_TABLE_NAME = "news"
const val NEWS_TABLE_LINK = "link"
const val NEWS_TABLE_AUTHOR_ID = "author_id"
const val NEWS_TABLE_TITLE = "title"
const val NEWS_TABLE_DESCRIPTION = "description"
const val NEWS_TABLE_FEATURED_MEDIA = "featured_media"
const val NEWS_TABLE_SERVER_ID = "server_id"
const val NEWS_TABLE_PUBLISHED_DATE = "published_date"

@Parcelize
@Entity(
    tableName = NEWS_TABLE_TABLE_NAME,

    foreignKeys = [(ForeignKey(
        entity = Author::class,
        parentColumns = [AUTHOR_TABLE_LINK],
        childColumns = [NEWS_TABLE_AUTHOR_ID]
    ))],

    indices = [(Index(value = [NEWS_TABLE_AUTHOR_ID]))]
)
data class News(

    @PrimaryKey
    @ColumnInfo(name = NEWS_TABLE_LINK)
    val link: String,

    @ColumnInfo(name = NEWS_TABLE_AUTHOR_ID)
    val authorID: String,

    @ColumnInfo(name = NEWS_TABLE_SERVER_ID)
    var serverID: Int,

    @ColumnInfo(name = NEWS_TABLE_FEATURED_MEDIA)
    var mediaFeaturedURL: String? = null,

    @ColumnInfo(name = NEWS_TABLE_TITLE)
    var title: String? = null,

    @ColumnInfo(name = NEWS_TABLE_DESCRIPTION)
    var description: String? = null,

    @ColumnInfo(name = NEWS_TABLE_PUBLISHED_DATE)
    var publishedDate: LocalDateTime? = null
) : Parcelable {

    constructor(selfHostedWPPost: SelfHostedWPPost) : this(
        selfHostedWPPost.link,
        selfHostedWPPost.authorLink,
        selfHostedWPPost.serverID
    ) {
        this.title = selfHostedWPPost.title[SELF_HOSTED_WP_POST_SERIALIZED_RENDERED]
        this.description = selfHostedWPPost.content[SELF_HOSTED_WP_POST_SERIALIZED_RENDERED]
        this.mediaFeaturedURL = selfHostedWPPost.featuredMediaLink
        this.publishedDate = selfHostedWPPost.date
    }
}