package at.ict4d.ict4dnews.models.rss

import org.simpleframework.xml.Attribute
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

const val WP_RSS_MEDIA_SERIALIZED_WP_RSS_MEDIA = "content"
const val WP_RSS_MEDIA_SERIALIZED_URL = "url"
const val WP_RSS_MEDIA_SERIALIZED_MEDIUM = "medium"

const val WP_RSS_MEDIA_SERIALIZED_PREFIX_MEDIA = "media"

@Namespace(prefix = WP_RSS_MEDIA_SERIALIZED_PREFIX_MEDIA)
@Root(name = WP_RSS_MEDIA_SERIALIZED_WP_RSS_MEDIA, strict = false)
data class WPRSSMedia(

    @field:Attribute(
        name = WP_RSS_MEDIA_SERIALIZED_URL,
        required = false)
    var url: String? = null,

    @field:Attribute(
        name = WP_RSS_MEDIA_SERIALIZED_MEDIUM,
        required = false)
    var medium: String? = null
)