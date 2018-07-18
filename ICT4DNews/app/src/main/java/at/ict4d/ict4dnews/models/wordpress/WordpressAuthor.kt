package at.ict4d.ict4dnews.models.wordpress

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor.Companion.TABLE_TABLE_NAME
import com.google.gson.annotations.SerializedName

@Entity(tableName = TABLE_TABLE_NAME)
data class WordpressAuthor(

    @PrimaryKey
    @ColumnInfo(name = TABLE_LINK)
    @SerializedName(SERIALIZED_LINK)
    val link: String,

    @ColumnInfo(name = TABLE_SERVER_ID)
    @SerializedName(SERIALIZED_SERVER_ID)
    val server_id: Int,

    @ColumnInfo(name = TABLE_NAME)
    @SerializedName(SERIALIZED_NAME)
    val name: String,

    @ColumnInfo(name = TABLE_URL)
    @SerializedName(SERIALIZED_URL)
    val url: String,

    @ColumnInfo(name = TABLE_DESCRIPTION)
    @SerializedName(SERIALIZED_DESCRIPTION)
    val description: String,

    @ColumnInfo(name = TABLE_SLUG)
    @SerializedName(SERIALIZED_SLUG)
    val slug: String,

    @ColumnInfo(name = TABLE_AVATAR_URLS)
    @SerializedName(SERIALIZED_AVATAR_URLS)
    val avatarURLs: Map<String, String>
) {
    companion object {
        const val TABLE_TABLE_NAME = "wordpress_authors"
        const val TABLE_SERVER_ID = "server_id"
        const val TABLE_NAME = "name"
        const val TABLE_URL = "url"
        const val TABLE_DESCRIPTION = "description"
        const val TABLE_SLUG = "slug"
        const val TABLE_AVATAR_URLS = "avatar_urls"
        const val TABLE_LINK = "link"

        const val SERIALIZED_SERVER_ID = "id"
        const val SERIALIZED_NAME = "name"
        const val SERIALIZED_URL = "url"
        const val SERIALIZED_DESCRIPTION = "description"
        const val SERIALIZED_SLUG = "slug"
        const val SERIALIZED_AVATAR_URLS = "avatar_urls"
        const val SERIALIZED_LINK = "link"
    }
}