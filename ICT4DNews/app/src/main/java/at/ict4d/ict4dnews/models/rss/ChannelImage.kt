package at.ict4d.ict4dnews.models.rss

import at.ict4d.ict4dnews.models.rss.ChannelImage.Companion.SERIALIZED_IMAGE
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Root(name = SERIALIZED_IMAGE, strict = false)
data class ChannelImage(
        @field:Element(name = SERIALIZED_URL)
        var url: String? = null,

        @field:Element(name = SERIALIZED_TITLE)
        var title: String? = null,

        @field:Element(name = SERIALIZED_LINK)
        var link: String? = null,

        @field:Element(name = SERIALIZED_WIDTH)
        var width: String? = null,

        @field:Element(name = SERIALIZED_HEIGHT)
        var height: String? = null
) {
    companion object {
        const val SERIALIZED_IMAGE = "image"
        const val SERIALIZED_URL = "url"
        const val SERIALIZED_TITLE = "title"
        const val SERIALIZED_LINK = "link"
        const val SERIALIZED_WIDTH = "width"
        const val SERIALIZED_HEIGHT = "height"
    }
}