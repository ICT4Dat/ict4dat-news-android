package at.ict4d.ict4dnews.models.wordpress

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.Index
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia.Companion.TABLE_AUTHOR
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia.Companion.TABLE_POST_LINK
import at.ict4d.ict4dnews.models.wordpress.WordpressMedia.Companion.TABLE_TABLE_NAME
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

@Entity(tableName = TABLE_TABLE_NAME,

        foreignKeys = [
            ForeignKey(entity = WordpressAuthor::class,
                    parentColumns = [WordpressAuthor.TABLE_LINK],
                    childColumns = [TABLE_AUTHOR]),
            ForeignKey(entity = SelfHostedWPPost::class,
                    parentColumns = [SelfHostedWPPost.TABLE_LINK],
                    childColumns = [(TABLE_POST_LINK)])
        ],

        indices = [
            Index(value = [TABLE_AUTHOR]),
            Index(value = [TABLE_POST_LINK])
        ])
data class WordpressMedia(

    @PrimaryKey
    @ColumnInfo(name = TABLE_WP_LINK)
    @SerializedName(SERIALIZED_WP_LINK)
    val wpLink: String,

    @ColumnInfo(name = TABLE_SERVER_ID)
    @SerializedName(SERIALIZED_SERVER_ID)
    val serverID: Int,

    @ColumnInfo(name = TABLE_DATE_CREATED)
    @SerializedName(SERIALIZED_DATE_CREATED)
    val dateCreated: LocalDateTime,

    @ColumnInfo(name = TABLE_RAW_LINK)
    @SerializedName(SERIALIZED_RAW_LINK)
    val linkRaw: String,

    @ColumnInfo(name = TABLE_SERVER_POST_ID)
    @SerializedName(SERIALIZED_SERVER_POST_ID)
    val serverPostID: Int,

    @ColumnInfo(name = TABLE_POST_LINK)
    var postLink: String,

    @ColumnInfo(name = TABLE_DATE_MODIFIED)
    @SerializedName(SERIALIZED_DATE_MODIFIED)
    val dateModified: LocalDateTime,

    @ColumnInfo(name = TABLE_SLUG)
    @SerializedName(SERIALIZED_SLUG)
    val slug: String,

    @ColumnInfo(name = TABLE_STATUS)
    @SerializedName(SERIALIZED_STATUS)
    val status: String,

    @ColumnInfo(name = TABLE_TYPE)
    @SerializedName(SERIALIZED_TYPE)
    val type: String,

    @Embedded
    @SerializedName(SERIALIZED_MEDIA_TITLE)
    val title: MediaTitle,

    @ColumnInfo(name = TABLE_SERVER_AUTHOR)
    @SerializedName(SERIALIZED_SERVER_AUTHOR)
    val serverAuthor: Int,

    @ColumnInfo(name = TABLE_AUTHOR)
    var authorLink: String,

    @ColumnInfo(name = TABLE_COMMENT_STATUS)
    @SerializedName(SERIALIZED_COMMENT_STATUS)
    val commentStatus: String,

    @Embedded
    @SerializedName(SERIALIZED_MEDIA_DESCRIPTION)
    val description: MediaDescription,

    @ColumnInfo(name = TABLE_ALT_TEXT)
    @SerializedName(SERIALIZED_ALT_TEXT)
    val altText: String,

    @ColumnInfo(name = TABLE_MEDIA_TYPE)
    @SerializedName(SERIALIZED__MEDIA_TYPE)
    val mediaType: String,

    @ColumnInfo(name = TABLE_MIME_TYPE)
    @SerializedName(SERIALIZED__MIME_TYPE)
    val mimeType: String

) {
    companion object {
        const val TABLE_TABLE_NAME = "wordpress_media"
        const val TABLE_WP_LINK = "link"
        const val TABLE_SERVER_ID = "serverID"
        const val TABLE_RAW_LINK = "raw_link"
        const val TABLE_SERVER_POST_ID = "server_post_id"
        const val TABLE_POST_LINK = "post_link"
        const val TABLE_DATE_CREATED = "date_created"
        const val TABLE_DATE_MODIFIED = "date_modified"
        const val TABLE_SLUG = "slug"
        const val TABLE_STATUS = "status"
        const val TABLE_TYPE = "type"
        const val TABLE_SERVER_AUTHOR = "server_author"
        const val TABLE_AUTHOR = "author"
        const val TABLE_COMMENT_STATUS = "comment_status"
        const val TABLE_ALT_TEXT = "alt_text"
        const val TABLE_MEDIA_TYPE = "media_type"
        const val TABLE_MIME_TYPE = "mime_type"

        const val SERIALIZED_WP_LINK = "link"
        const val SERIALIZED_SERVER_ID = "id"
        const val SERIALIZED_SERVER_POST_ID = "post"
        const val SERIALIZED_DATE_CREATED = "date"
        const val SERIALIZED_RAW_LINK = "source_url"
        const val SERIALIZED_DATE_MODIFIED = "modified"
        const val SERIALIZED_SLUG = "slug"
        const val SERIALIZED_STATUS = "status"
        const val SERIALIZED_TYPE = "type"
        const val SERIALIZED_MEDIA_TITLE = "title"
        const val SERIALIZED_SERVER_AUTHOR = "author"
        const val SERIALIZED_COMMENT_STATUS = "comment_status"
        const val SERIALIZED_MEDIA_DESCRIPTION = "description"
        const val SERIALIZED_ALT_TEXT = "alt_text"
        const val SERIALIZED__MEDIA_TYPE = "media_type"
        const val SERIALIZED__MIME_TYPE = "mime_type"
    }
}

data class MediaTitle(
    @ColumnInfo(name = TABLE_TITLE_RENDERED)
    @SerializedName(SERIALIZED_TITLE_RENDERED)
    var rendered: String
) {
    companion object {
        const val TABLE_TITLE_RENDERED = "title_rendered"
        const val SERIALIZED_TITLE_RENDERED = "rendered"
    }
}

data class MediaDescription(
    @ColumnInfo(name = TABLE_DESCRIPTION_RENDERED)
    @SerializedName(SERIALIZED_DESCRIPTION_RENDERED)
    var rendered: String
) {
    companion object {
        const val TABLE_DESCRIPTION_RENDERED = "description_rendered"
        const val SERIALIZED_DESCRIPTION_RENDERED = "rendered"
    }
}
