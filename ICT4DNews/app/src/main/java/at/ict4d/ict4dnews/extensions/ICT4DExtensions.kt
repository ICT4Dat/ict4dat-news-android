package at.ict4d.ict4dnews.extensions

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.databinding.BindingAdapter
import android.graphics.drawable.Drawable
import android.net.Uri
import android.support.annotation.DrawableRes
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.utils.GlideApp
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
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

@BindingAdapter(value = ["loadFromURL", "placeholder", "error", "round"], requireAll = false)
fun ImageView.loadFromURL(
    url: String,
    @DrawableRes
    placeholder: Int = R.drawable.ic_refresh_black_24dp,
    @DrawableRes
    error: Int = R.drawable.ic_broken_image_black_24dp,
    round: Boolean = false
) {
    val glide = GlideApp.with(context).load(url)

    if (round) {
        glide.apply(RequestOptions.circleCropTransform())
    }

    var imagePlaceholder = placeholder
    if (imagePlaceholder == 0) { // is 0 when not set through XML
        imagePlaceholder = R.drawable.ic_refresh_black_24dp
    }

    var imageErrorPlaceholder = error
    if (imageErrorPlaceholder == 0) { // is 0 when not set through XML
        imageErrorPlaceholder = R.drawable.ic_broken_image_black_24dp
    }

    glide
        .placeholder(imagePlaceholder)
        .error(imageErrorPlaceholder)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                if (model != null && model.toString().isNotEmpty()) {
                    Timber.w(e, "Failed to load image with $model")
                }
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean = false
        })
        .into(this)
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun Context.contactDevelopers(email: String) {
    val intent = Intent(Intent.ACTION_SENDTO)
    val subject = "Feedback from ICT4d.at News App"
    intent.data = Uri.parse("mailto:" + email)
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    startActivity(Intent.createChooser(intent, "Send email via: "))
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