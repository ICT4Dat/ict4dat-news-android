package at.ict4d.ict4dnews.models.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

const val RSSFEED_SERIALIZED_RSS = "rss"

@Root(name = RSSFEED_SERIALIZED_RSS, strict = false)
data class RSSFeed(

    @field:Element(
        name = CHANNEL_SERIALIZED_CHANNEL,
        required = false)
    var channel: Channel? = null
)