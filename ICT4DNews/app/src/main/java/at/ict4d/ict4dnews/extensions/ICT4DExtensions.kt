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

fun String.toLocalDateTimeFromRFCString(): LocalDateTime? =
    LocalDateTime.parse(this, DateTimeFormatter.RFC_1123_DATE_TIME) ?: null

@BindingAdapter("loadCircularImage")
fun ImageView.loadCircularImage(imageUrl: String?) {
    imageUrl?.let {
        setImageDrawable(null)
        // TODO(Change error and placeholder image)
        val requestOptions = RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.drawable.ic_error_black_24dp)
            .apply(RequestOptions.circleCropTransform())
        Glide.with(this.context).load(it)
            .apply(requestOptions)
            .into(this)
    }
}

@BindingAdapter("loadImage")
fun ImageView.loadImage(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(this.context).load(it).into(this)
    }
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