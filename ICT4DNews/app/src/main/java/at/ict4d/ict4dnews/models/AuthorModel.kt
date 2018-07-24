package at.ict4d.ict4dnews.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import at.ict4d.ict4dnews.models.wordpress.WordpressAuthor

const val AUTHOR_TABLE_TABLE_NAME = "authors"
const val AUTHOR_TABLE_LINK = "link"
const val AUTHOR_TABLE_SERVER_ID = "server_id"
const val AUTHOR_TABLE_NAME = "name"
const val AUTHOR_TABLE_DESCRIPTION = "description"
const val AUTHOR_TABLE_USERNAME = "username"
const val AUTHOR_TABLE_IMAGE_URL = "image_url"

@Entity(tableName = AUTHOR_TABLE_TABLE_NAME)
data class AuthorModel(

    @PrimaryKey
    @ColumnInfo(name = AUTHOR_TABLE_LINK)
    val link: String,

    @ColumnInfo(name = AUTHOR_TABLE_SERVER_ID)
    val serverID: Int,

    @ColumnInfo(name = AUTHOR_TABLE_NAME)
    var name: String? = null,

    @ColumnInfo(name = AUTHOR_TABLE_IMAGE_URL)
    var imageURL: String? = null,

    @ColumnInfo(name = AUTHOR_TABLE_DESCRIPTION)
    var description: String? = null,

    @ColumnInfo(name = AUTHOR_TABLE_USERNAME)
    var username: String? = null
) {

    constructor(serverAuthor: WordpressAuthor): this(serverAuthor.link, serverAuthor.server_id) {
        name = serverAuthor.name
        imageURL = serverAuthor.avatarURLs.values.lastOrNull()
        description = serverAuthor.description
        username = serverAuthor.slug
    }
}