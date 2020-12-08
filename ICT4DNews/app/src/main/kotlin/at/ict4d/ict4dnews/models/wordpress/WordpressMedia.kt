package at.ict4d.ict4dnews.models.wordpress

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

const val WORDPRESS_MEDIA_SERIALIZED_WP_LINK = "link"
const val WORDPRESS_MEDIA_SERIALIZED_SERVER_ID = "id"
const val WORDPRESS_MEDIA_SERIALIZED_SERVER_POST_ID = "post"
const val WORDPRESS_MEDIA_SERIALIZED_DATE_CREATED = "date"
const val WORDPRESS_MEDIA_SERIALIZED_RAW_LINK = "source_url"
const val WORDPRESS_MEDIA_SERIALIZED_DATE_MODIFIED = "modified"
const val WORDPRESS_MEDIA_SERIALIZED_SLUG = "slug"
const val WORDPRESS_MEDIA_SERIALIZED_STATUS = "status"
const val WORDPRESS_MEDIA_SERIALIZED_TYPE = "type"
const val WORDPRESS_MEDIA_SERIALIZED_MEDIA_TITLE = "title"
const val WORDPRESS_MEDIA_SERIALIZED_SERVER_AUTHOR = "author"
const val WORDPRESS_MEDIA_SERIALIZED_COMMENT_STATUS = "comment_status"
const val WORDPRESS_MEDIA_SERIALIZED_MEDIA_DESCRIPTION = "description"
const val WORDPRESS_MEDIA_SERIALIZED_ALT_TEXT = "alt_text"
const val WORDPRESS_MEDIA_SERIALIZED__MEDIA_TYPE = "media_type"
const val WORDPRESS_MEDIA_SERIALIZED__MIME_TYPE = "mime_type"

const val MEDIA_TITLE_SERIALIZED_TITLE_RENDERED = "rendered"

const val MEDIA_DESCRIPTION_SERIALIZED_DESCRIPTION_RENDERED = "rendered"

data class WordpressMedia(

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_WP_LINK)
    val wpLink: String,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_SERVER_ID)
    val serverID: Int,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_DATE_CREATED)
    val dateCreated: LocalDateTime,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_RAW_LINK)
    val linkRaw: String,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_SERVER_POST_ID)
    val serverPostID: Int,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_DATE_MODIFIED)
    val dateModified: LocalDateTime,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_SLUG)
    val slug: String,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_STATUS)
    val status: String,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_TYPE)
    val type: String,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_MEDIA_TITLE)
    val title: MediaTitle,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_SERVER_AUTHOR)
    val serverAuthorID: Int,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_COMMENT_STATUS)
    val commentStatus: String,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_MEDIA_DESCRIPTION)
    val description: MediaDescription,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED_ALT_TEXT)
    val altText: String,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED__MEDIA_TYPE)
    val mediaType: String,

    @SerializedName(WORDPRESS_MEDIA_SERIALIZED__MIME_TYPE)
    val mimeType: String,

    var postLink: String?,

    var authorLink: String?
)

data class MediaTitle(
    @SerializedName(MEDIA_TITLE_SERIALIZED_TITLE_RENDERED)
    var rendered: String
)

data class MediaDescription(
    @SerializedName(MEDIA_DESCRIPTION_SERIALIZED_DESCRIPTION_RENDERED)
    var rendered: String
)
