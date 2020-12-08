package at.ict4d.ict4dnews.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia
import java.time.LocalDateTime

const val MEDIA_TABLE_TABLE_NAME = "media"
const val MEDIA_TABLE_LINK = "link"
const val MEDIA_TABLE_SERVER_ID = "serverID"
const val MEDIA_TABLE_TITLE = "title"
const val MEDIA_TABLE_DESCRIPTION = "description"
const val MEDIA_TABLE_NEWS_ID = "news_id"
const val MEDIA_TABLE_DATE_CREATED = "date_created"
const val MEDIA_TABLE_AUTHOR_ID = "author_id"
const val MEDIA_TABLE_MIME_TYPE = "mime_type"

@Entity(
    tableName = MEDIA_TABLE_TABLE_NAME,

    foreignKeys = [
        ForeignKey(
            entity = News::class,
            parentColumns = [NEWS_TABLE_LINK],
            childColumns = [MEDIA_TABLE_NEWS_ID]
        ),
        ForeignKey(
            entity = Author::class,
            parentColumns = [AUTHOR_TABLE_LINK],
            childColumns = [MEDIA_TABLE_AUTHOR_ID]
        )],

    indices = [(Index(value = [MEDIA_TABLE_NEWS_ID])),
        Index(value = [MEDIA_TABLE_AUTHOR_ID])]
)
data class Media(

    @PrimaryKey
    @ColumnInfo(name = MEDIA_TABLE_LINK)
    val link: String,

    @ColumnInfo(name = MEDIA_TABLE_SERVER_ID)
    val serverID: Int?,

    @ColumnInfo(name = MEDIA_TABLE_NEWS_ID)
    val newsID: String?,

    @ColumnInfo(name = MEDIA_TABLE_AUTHOR_ID)
    val authorID: String?,

    @ColumnInfo(name = MEDIA_TABLE_MIME_TYPE)
    var mediaType: String? = null,

    @ColumnInfo(name = MEDIA_TABLE_TITLE)
    var title: String? = null,

    @ColumnInfo(name = MEDIA_TABLE_DESCRIPTION)
    var description: String? = null,

    @ColumnInfo(name = MEDIA_TABLE_DATE_CREATED)
    var dateCreated: LocalDateTime? = null
) {

    constructor(serverMedia: WordpressMedia) : this(
        serverMedia.linkRaw,
        serverMedia.serverID,
        serverMedia.postLink,
        serverMedia.authorLink,
        serverMedia.mimeType,
        serverMedia.title.rendered,
        serverMedia.description.rendered,
        serverMedia.dateCreated
    )
}
