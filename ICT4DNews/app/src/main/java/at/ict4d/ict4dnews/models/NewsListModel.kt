package at.ict4d.ict4dnews.models

import android.os.Parcelable
import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NewsListModel(
    val forListNewsURL: String,
    var forListTitle: String? = null,
    var forListDescription: String? = null,
    var forListImageURL: String? = null
) : Parcelable {

    constructor(selfHostedWPPost: SelfHostedWPPost) : this(selfHostedWPPost.link) {
        this.forListTitle = selfHostedWPPost.title[SelfHostedWPPost.SERIALIZED_RENDERED]
        this.forListDescription = selfHostedWPPost.content[SelfHostedWPPost.SERIALIZED_RENDERED]
    }
}