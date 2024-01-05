package at.ict4d.ict4dnews.models.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

const val CHANNEL_SERIALIZED_CHANNEL = "channel"
const val CHANNEL_SERIALIZED_TITLE = "title"
const val CHANNEL_SERIALIZED_DESCRIPTION = "description"
const val CHANNEL_SERIALIZED_LAST_BUILD_DATE = "lastBuildDate"
const val CHANNEL_SERIALIZED_LANGUAGE = "language"
const val CHANNEL_SERIALIZED_GENERATOR = "generator"

@Root(name = CHANNEL_SERIALIZED_CHANNEL, strict = false)
data class Channel(

    @field:Element(
        name = CHANNEL_SERIALIZED_TITLE,
        required = false,
    )
    var title: String? = null,

    @field:Element(
        name = CHANNEL_SERIALIZED_DESCRIPTION,
        required = false,
    )
    var description: String? = null,

    @field:Element(
        name = CHANNEL_SERIALIZED_LAST_BUILD_DATE,
        required = false,
    )
    var lastBuildDate: String? = null,

    @field:Element(
        name = CHANNEL_SERIALIZED_LANGUAGE,
        required = false,
    )
    var language: String? = null,

    @field:Element(
        name = CHANNEL_SERIALIZED_GENERATOR,
        required = false,
    )
    var generator: String? = null,

    @field:Element(
        name = CHANNEL_IMAGE_SERIALIZED_IMAGE,
        required = false,
    )
    var image: ChannelImage? = null,

    @field:ElementList(
        inline = true,
        name = FEED_ITEM_SERIALIZED_FEED_ITEM,
        required = false,
    )
    var feedItems: List<FeedItem>? = null,
)
