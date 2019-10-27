package at.ict4d.ict4dnews.models.wordpress

import com.google.gson.annotations.SerializedName

const val WORDPRESS_AUTHOR_SERIALIZED_SERVER_ID = "id"
const val WORDPRESS_AUTHOR_SERIALIZED_NAME = "name"
const val WORDPRESS_AUTHOR_SERIALIZED_URL = "url"
const val WORDPRESS_AUTHOR_SERIALIZED_DESCRIPTION = "description"
const val WORDPRESS_AUTHOR_SERIALIZED_SLUG = "slug"
const val WORDPRESS_AUTHOR_SERIALIZED_AVATAR_URLS = "avatar_urls"
const val WORDPRESS_AUTHOR_SERIALIZED_LINK = "link"

data class WordpressAuthor(

    @SerializedName(WORDPRESS_AUTHOR_SERIALIZED_LINK)
    val link: String,

    @SerializedName(WORDPRESS_AUTHOR_SERIALIZED_SERVER_ID)
    val server_id: Int,

    @SerializedName(WORDPRESS_AUTHOR_SERIALIZED_NAME)
    val name: String,

    @SerializedName(WORDPRESS_AUTHOR_SERIALIZED_URL)
    val url: String,

    @SerializedName(WORDPRESS_AUTHOR_SERIALIZED_DESCRIPTION)
    val description: String,

    @SerializedName(WORDPRESS_AUTHOR_SERIALIZED_SLUG)
    val slug: String,

    @SerializedName(WORDPRESS_AUTHOR_SERIALIZED_AVATAR_URLS)
    val avatarURLs: Map<String, String>
)
