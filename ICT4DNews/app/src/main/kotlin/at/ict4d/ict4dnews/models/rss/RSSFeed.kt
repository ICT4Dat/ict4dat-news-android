package at.ict4d.ict4dnews.models.rss

import at.ict4d.ict4dnews.models.rss.Channel.Companion.SERIALIZED_CHANNEL
import at.ict4d.ict4dnews.models.rss.RSSFeed.Companion.SERIALIZED_RSS
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = SERIALIZED_RSS, strict = false)
data class RSSFeed(
    @field:Element(name = SERIALIZED_CHANNEL)
    var channel: Channel? = null
) {
    companion object {
        const val SERIALIZED_RSS = "rss"
    }
}