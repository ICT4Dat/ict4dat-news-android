package at.ict4d.ict4dnews.models.rss

import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

const val CHANNEL_IMAGE_SERIALIZED_IMAGE = "image"
const val CHANNEL_IMAGE_SERIALIZED_URL = "url"
const val CHANNEL_IMAGE_SERIALIZED_TITLE = "title"
const val CHANNEL_IMAGE_SERIALIZED_LINK = "link"
const val CHANNEL_IMAGE_SERIALIZED_WIDTH = "width"
const val CHANNEL_IMAGE_SERIALIZED_HEIGHT = "height"

@Root(name = CHANNEL_IMAGE_SERIALIZED_IMAGE, strict = false)
data class ChannelImage(

    @field:Element(
        name = CHANNEL_IMAGE_SERIALIZED_URL,
        required = false
    )
    var url: String? = null,

    @field:Element(
        name = CHANNEL_IMAGE_SERIALIZED_TITLE,
        required = false
    )
    var title: String? = null,

    @field:Element(
        name = CHANNEL_IMAGE_SERIALIZED_LINK,
        required = false
    )
    var link: String? = null,

    @field:Element(
        name = CHANNEL_IMAGE_SERIALIZED_WIDTH,
        required = false
    )
    var width: String? = null,

    @field:Element(
        name = CHANNEL_IMAGE_SERIALIZED_HEIGHT,
        required = false
    )
    var height: String? = null
)
