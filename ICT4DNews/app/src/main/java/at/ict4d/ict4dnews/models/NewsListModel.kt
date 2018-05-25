package at.ict4d.ict4dnews.models

import at.ict4d.ict4dnews.models.wordpress.SelfHostedWPPost

data class NewsListModel(val forListNewsURL: String) {

    var forListTitle: String? = null
    var forListDescription: String? = null
    var forListImageURL: String? = null

    constructor(forListTitle: String?,
                forListDescription: String?,
                forListNewsURL: String,
                forListImageURL: String?): this (forListNewsURL) {
        this.forListTitle = forListTitle
        this.forListDescription = forListDescription
        this.forListImageURL = forListImageURL

    }

    constructor(selfHostedWPPost: SelfHostedWPPost): this(selfHostedWPPost.link) {
        this.forListTitle = selfHostedWPPost.title[SelfHostedWPPost.SERIALIZED_RENDERED]
        this.forListDescription = selfHostedWPPost.content[SelfHostedWPPost.SERIALIZED_RENDERED]
    }

}