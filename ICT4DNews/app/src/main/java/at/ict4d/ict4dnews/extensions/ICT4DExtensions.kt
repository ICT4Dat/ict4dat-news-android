package at.ict4d.ict4dnews.extensions

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.databinding.BindingAdapter
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import at.ict4d.ict4dnews.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

fun Context.browseCustomTab(url: String) {
    CustomTabsIntent
        .Builder()
        .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
        .build()
        .launchUrl(this, Uri.parse(url))
}

fun LocalDateTime.extractDate(): String = this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))

@BindingAdapter("showDate")
fun TextView.showDate(localDateTime: LocalDateTime?) {
    text = localDateTime?.extractDate()
}

fun String.toLocalDateTimeFromRFCString(): LocalDateTime? =
    LocalDateTime.parse(this, DateTimeFormatter.RFC_1123_DATE_TIME) ?: null

@BindingAdapter("loadCircularImage")
fun ImageView.loadCircularImage(imageUrl: String?) {
    loadImageHelper(imageUrl, true)
}

@BindingAdapter(value = ["loadImage", "enableRoundImage"], requireAll = true)
fun ImageView.loadImage(imageUrl: String?, enableRoundImage: Boolean = false) {
    loadImageHelper(imageUrl, enableRoundImage)
}

fun ImageView.loadImageHelper(imageUrl: String?, enableRoundImage: Boolean = true) {
    // TODO(Change error and placeholder image)

    var requestOptions = RequestOptions()
        .placeholder(R.drawable.ic_refresh_black_24dp)
        .error(R.drawable.ic_broken_image_black_24dp)

    if (enableRoundImage) {
        requestOptions = requestOptions.apply(RequestOptions.circleCropTransform())
    }
    Glide.with(this.context)
        .load(imageUrl)
        .apply(requestOptions)
        .into(this)
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

/**
 * LiveData that propagates only distinct emissions.
 * @see https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1
 */
fun <T> LiveData<T>.getDistinct(): LiveData<T> {
    val distinctLiveData = MediatorLiveData<T>()
    distinctLiveData.addSource(this, object : Observer<T> {
        private var initialized = false
        private var lastObj: T? = null
        override fun onChanged(obj: T?) {
            if (!initialized) {
                initialized = true
                lastObj = obj
                distinctLiveData.postValue(lastObj)
            } else if ((obj == null && lastObj != null) ||
                obj != lastObj
            ) {
                lastObj = obj
                distinctLiveData.postValue(lastObj)
            }
        }
    })
    return distinctLiveData
}