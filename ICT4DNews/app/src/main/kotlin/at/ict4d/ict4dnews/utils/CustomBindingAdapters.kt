package at.ict4d.ict4dnews.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import at.ict4d.ict4dnews.extensions.visible
import org.threeten.bp.LocalDateTime

@BindingAdapter("newsPublishDateTime", "mostRecentNewsPublishDateTime", requireAll = true)
fun isNewsIsNew(
    imageView: AppCompatImageView,
    newsPublishDateTime: LocalDateTime?,
    mostRecentNewsPublishDateTime: LocalDateTime?
) {
    imageView.visible(
        if (mostRecentNewsPublishDateTime == null) {
            true
        } else {
            newsPublishDateTime?.isAfter(mostRecentNewsPublishDateTime) ?: false
        }
    )
}