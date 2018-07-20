package at.ict4d.ict4dnews.models.wordpress

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

const val SELF_HOSTED_WP_POST_SERIALIZED_ID = "id"
const val SELF_HOSTED_WP_POST_SERIALIZED_DATE = "date"
const val SELF_HOSTED_WP_POST_SERIALIZED_MODIFIED = "modified"
const val SELF_HOSTED_WP_POST_SERIALIZED_SLUG = "slug"
const val SELF_HOSTED_WP_POST_SERIALIZED_TYPE = "type"
const val SELF_HOSTED_WP_POST_SERIALIZED_LINK = "link"
const val SELF_HOSTED_WP_POST_SERIALIZED_TITLE = "title"
const val SELF_HOSTED_WP_POST_SERIALIZED_CONTENT = "content"
const val SELF_HOSTED_WP_POST_SERIALIZED_EXCERPT = "excerpt"
const val SELF_HOSTED_WP_POST_SERIALIZED_SERVER_AUTHOR = "author"
const val SELF_HOSTED_WP_POST_SERIALIZED_FEATURED_MEDIA = "featured_media"
const val SELF_HOSTED_WP_POST_SERIALIZED_COMMENT_STATUS = "comment_status"
const val SELF_HOSTED_WP_POST_SERIALIZED_CATEGORIES = "categories"
const val SELF_HOSTED_WP_POST_SERIALIZED_TAGS = "tags"
const val SELF_HOSTED_WP_POST_SERIALIZED_RENDERED = "rendered"

data class SelfHostedWPPost(

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_LINK)
        val link: String,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_ID)
        val serverID: Int,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_DATE)
        val date: LocalDateTime,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_MODIFIED)
        val modifiedDate: LocalDateTime,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_SLUG)
        val slug: String,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_TYPE)
        val type: String,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_TITLE)
        val title: MutableMap<String, String>,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_CONTENT)
        val content: MutableMap<String, String>,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_EXCERPT)
        val excerpt: Map<String, String>,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_SERVER_AUTHOR)
        val serverAuthorID: Int,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_FEATURED_MEDIA)
        val featuredMedia: Int,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_COMMENT_STATUS)
        val commentStatus: String,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_CATEGORIES)
        val categories: List<Int>,

        @SerializedName(SELF_HOSTED_WP_POST_SERIALIZED_TAGS)
        val tags: List<Int>,

        var authorLink: String,

        var featuredMediaLink: String
)