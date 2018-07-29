package at.ict4d.ict4dnews.extensions

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.Observer
import android.databinding.BindingAdapter
import android.text.Html
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.extractDate(): String {
    return this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))
}

@BindingAdapter("loadCircularImage")
fun ImageView.loadCircularImage(imageUrl: String?) {
    imageUrl?.let {
        Glide.with(this.context).load(it).apply(RequestOptions.circleCropTransform()).into(this)
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

@Suppress("DEPRECATION")
fun String.stripHtml(): String {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY).toString()
    } else {
        Html.fromHtml(this).toString()
    }
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