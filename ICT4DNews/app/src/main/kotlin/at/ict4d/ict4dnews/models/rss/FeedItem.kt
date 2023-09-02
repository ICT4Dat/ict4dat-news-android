package at.ict4d.ict4dnews.models.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root

const val FEED_ITEM_SERIALIZED_FEED_ITEM = "item"
const val FEED_ITEM_SERIALIZED_PUB_DATE = "pubDate"
const val FEED_ITEM_SERIALIZED_TITLE = "title"
const val FEED_ITEM_SERIALIZED_LINK = "link"
const val FEED_ITEM_SERIALIZED_DESCRIPTION = "description"
const val FEED_ITEM_SERIALIZED_CONTENT = "encoded"
const val FEED_ITEM_SERIALIZED_WP_Media = "content"

const val FEED_ITEM_SERIALIZED_PREFIX_CONTENT = "content"
const val FEED_ITEM_SERIALIZED_PREFIX_WP_Media = "media"

@Root(name = FEED_ITEM_SERIALIZED_FEED_ITEM, strict = false)
data class FeedItem(
    @field:Element(
        name = FEED_ITEM_SERIALIZED_PUB_DATE,
        required = false
    )
    var pubDate: String? = null,

    @field:Element(
        name = FEED_ITEM_SERIALIZED_TITLE,
        required = false
    )
    var title: String? = null,

    @field:Element(
        name = FEED_ITEM_SERIALIZED_LINK,
        required = false
    )
    var link: String? = null,

    @field:Element(
        name = FEED_ITEM_SERIALIZED_DESCRIPTION,
        required = false
    )
    var description: String? = null,

    @Namespace(prefix = FEED_ITEM_SERIALIZED_PREFIX_CONTENT)
    @field:Element(
        name = FEED_ITEM_SERIALIZED_CONTENT,
        required = false
    )
    var wpContent: String? = null,

    @field:Namespace(prefix = FEED_ITEM_SERIALIZED_PREFIX_WP_Media)
    @field:ElementList(
        name = FEED_ITEM_SERIALIZED_WP_Media,
        required = false,
        inline = true
    )
    var wpRSSMedia: List<WPRSSMedia>? = null
)
