package at.ict4d.ict4dnews.models.wordpress

import android.arch.persistence.room.*
import at.ict4d.ict4dnews.models.NewsListModel
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost.Companion.TABLE_TABLE_NAME
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

@Entity(tableName = TABLE_TABLE_NAME,

        foreignKeys = [(ForeignKey(entity = WordpressAuthor::class,
                parentColumns = [WordpressAuthor.TABLE_LINK],
                childColumns = [SelfHostedWPPost.TABLE_AUTHOR]))],

        indices = [(Index(value = [SelfHostedWPPost.TABLE_AUTHOR]))])
data class SelfHostedWPPost (

        @PrimaryKey
        @ColumnInfo(name = TABLE_LINK)
        @SerializedName(SERIALIZED_LINK)
        val link: String,

        @ColumnInfo(name = TABLE_SERVER_ID)
        @SerializedName(SERIALIZED_ID)
        val serverID: Int,

        @ColumnInfo(name = TABLE_DATE)
        @SerializedName(SERIALIZED_DATE)
        val date: LocalDateTime,

        @ColumnInfo(name = TABLE_MODIFIED)
        @SerializedName(SERIALIZED_MODIFIED)
        val modifiedDate: LocalDateTime,

        @ColumnInfo(name = TABLE_SLUG)
        @SerializedName(SERIALIZED_SLUG)
        val slug: String,

        @ColumnInfo(name = TABLE_TYPE)
        @SerializedName(SERIALIZED_TYPE)
        val type: String,

        @ColumnInfo(name = TABLE_TITLE)
        @SerializedName(SERIALIZED_TITLE)
        val title: MutableMap<String, String>,

        @ColumnInfo(name = TABLE_CONTENT)
        @SerializedName(SERIALIZED_CONTENT)
        val content: MutableMap<String, String>,

        @ColumnInfo(name = TABLE_EXCERPT)
        @SerializedName(SERIALIZED_EXCERPT)
        val excerpt: Map<String, String>,

        @ColumnInfo(name = TABLE_SERVER_AUTHOR)
        @SerializedName(SERIALIZED_SERVER_AUTHOR)
        val serverAuthor: Int,

        @ColumnInfo(name = TABLE_AUTHOR)
        var authorLink: String,

        @ColumnInfo(name = TABLE_FEATURED_MEDIA)
        @SerializedName(SERIALIZED_FEATURED_MEDIA)
        val featuredMedia: Int,

        @ColumnInfo(name = TABLE_COMMENT_STATUS)
        @SerializedName(SERIALIZED_COMMENT_STATUS)
        val commentStatus: String,

        @ColumnInfo(name = TABLE_CATEGORIES)
        @SerializedName(SERIALIZED_CATEGORIES)
        val categories: List<Int>,

        @ColumnInfo(name = TABLE_TAGS)
        @SerializedName(SERIALIZED_TAGS)
        val tags: List<Int>

) {

    companion object {
        const val TABLE_TABLE_NAME = "self_hosted_wp_posts"
        const val TABLE_SERVER_ID = "server_id"
        const val TABLE_DATE = "date"
        const val TABLE_MODIFIED = "modified"
        const val TABLE_SLUG = "slug"
        const val TABLE_TYPE = "type"
        const val TABLE_LINK = "link"
        const val TABLE_TITLE = "title"
        const val TABLE_CONTENT = "content"
        const val TABLE_EXCERPT = "excerpt"
        const val TABLE_SERVER_AUTHOR = "server_author"
        const val TABLE_AUTHOR = "author"
        const val TABLE_FEATURED_MEDIA = "featured_media"
        const val TABLE_COMMENT_STATUS = "comment_status"
        const val TABLE_CATEGORIES = "categories"
        const val TABLE_TAGS = "tags"

        const val SERIALIZED_ID = "id"
        const val SERIALIZED_DATE = "date"
        const val SERIALIZED_MODIFIED = "modified"
        const val SERIALIZED_SLUG = "slug"
        const val SERIALIZED_TYPE = "type"
        const val SERIALIZED_LINK = "link"
        const val SERIALIZED_TITLE = "title"
        const val SERIALIZED_CONTENT = "content"
        const val SERIALIZED_EXCERPT = "excerpt"
        const val SERIALIZED_SERVER_AUTHOR = "author"
        const val SERIALIZED_FEATURED_MEDIA = "featured_media"
        const val SERIALIZED_COMMENT_STATUS = "comment_status"
        const val SERIALIZED_CATEGORIES = "categories"
        const val SERIALIZED_TAGS = "tags"
        const val SERIALIZED_RENDERED = "rendered"
    }
}