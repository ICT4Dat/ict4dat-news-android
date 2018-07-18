package at.ict4d.ict4dnews.models.rss

import at.ict4d.ict4dnews.models.rss.Channel.Companion.SERIALIZED_CHANNEL
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root

@Root(name = SERIALIZED_CHANNEL, strict = false)
data class Channel(
    @field:Element(name = SERIALIZED_TITLE)
    var title: String? = null,

    @field:Element(name = SERIALIZED_DESCRIPTION)
    var description: String? = null,

    @field:Element(name = SERIALIZED_LAST_BUILD_DATE)
    var lastBuildDate: String? = null,

    @field:Element(name = SERIALIZED_LANGUAGE)
    var language: String? = null,

    @field:Element(name = SERIALIZED_GENERATOR)
    var generator: String? = null,

    @field:Element(name = ChannelImage.SERIALIZED_IMAGE)
    var image: ChannelImage? = null,

    @field:ElementList(inline = true, name = "item")
    var feedItems: List<FeedItem>? = null
) {
    companion object {
        const val SERIALIZED_CHANNEL = "channel"
        const val SERIALIZED_TITLE = "title"
        const val SERIALIZED_DESCRIPTION = "description"
        const val SERIALIZED_LAST_BUILD_DATE = "lastBuildDate"
        const val SERIALIZED_LANGUAGE = "language"
        const val SERIALIZED_GENERATOR = "generator"
    }
}