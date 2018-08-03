package at.ict4d.ict4dnews.models.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = "item", strict = false)
data class FeedItem(
    @field:Element(name = "pubDate")
    var pubDate: String? = null,

    @field:Element(name = "title")
    var title: String? = null,

    @field:Element(name = "link")
    var link: String? = null,

    @field:Element(name = "description")
    var description: String? = null
)