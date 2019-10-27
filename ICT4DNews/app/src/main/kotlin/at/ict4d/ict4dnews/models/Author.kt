package at.ict4d.ict4dnews.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import at.ict4d.ict4dnews.models.rss.Channel
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor

const val AUTHOR_TABLE_TABLE_NAME = "authors"
const val AUTHOR_TABLE_LINK = "link"
const val AUTHOR_TABLE_SERVER_ID = "server_id"
const val AUTHOR_TABLE_NAME = "name"
const val AUTHOR_TABLE_DESCRIPTION = "description"
const val AUTHOR_TABLE_USERNAME = "username"
const val AUTHOR_TABLE_IMAGE_URL = "image_url"

@Entity(tableName = AUTHOR_TABLE_TABLE_NAME)
data class Author(

    @PrimaryKey
    @ColumnInfo(name = AUTHOR_TABLE_LINK)
    val link: String,

    @ColumnInfo(name = AUTHOR_TABLE_SERVER_ID)
    val serverID: Int?,

    @ColumnInfo(name = AUTHOR_TABLE_NAME)
    var name: String? = null,

    @ColumnInfo(name = AUTHOR_TABLE_IMAGE_URL)
    var imageURL: String? = null,

    @ColumnInfo(name = AUTHOR_TABLE_DESCRIPTION)
    var description: String? = null,

    @ColumnInfo(name = AUTHOR_TABLE_USERNAME)
    var username: String? = null
) {

    constructor(serverAuthor: WordpressAuthor) : this(serverAuthor.link, serverAuthor.server_id) {
        name = serverAuthor.name
        // Ignore Android Studio suggestion, the null checks are necessary!
        if (serverAuthor.avatarURLs != null && serverAuthor.avatarURLs.isNotEmpty() &&
            serverAuthor.avatarURLs.values != null && serverAuthor.avatarURLs.values.isNotEmpty()
        ) {
            imageURL = serverAuthor.avatarURLs.values.lastOrNull()
        } else {
            imageURL = null
        }
        description = serverAuthor.description
        username = serverAuthor.slug
    }

    constructor(blog: Blog, channel: Channel) : this(
        blog.feed_url,
        0,
        channel.title,
        channel.image?.url,
        channel.description,
        null
    )
}
