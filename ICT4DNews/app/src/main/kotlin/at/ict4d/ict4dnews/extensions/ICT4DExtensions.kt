package at.ict4d.ict4dnews.extensions

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.RecyclerView
import at.ict4d.ict4dnews.BuildConfig
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import org.jsoup.Jsoup
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.extractDate(): String = this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))

fun String.toLocalDateTimeFromRFCString(): LocalDateTime? =
    LocalDateTime.parse(this, DateTimeFormatter.RFC_1123_DATE_TIME) ?: null

@BindingAdapter(value = ["loadFromRes", "placeholder", "error", "round"], requireAll = false)
fun ImageView.loadFromURL(
    @DrawableRes
    drawableRes: Int,
    @DrawableRes
    placeholder: Int? = null,
    @DrawableRes
    error: Int? = null,
    round: Boolean = false
) = setUpGlideAndLoad(this, Glide.with(context).load(drawableRes), placeholder, error, round)

@BindingAdapter(value = ["loadFromURL", "placeholder", "error", "round"], requireAll = false)
fun ImageView.loadFromURL(
    url: String?,
    @DrawableRes
    placeholder: Int? = null,
    @DrawableRes
    error: Int? = null,
    round: Boolean = false
) = setUpGlideAndLoad(this, Glide.with(context).load(url), placeholder, error, round)

private val crossFadeFactory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

private fun setUpGlideAndLoad(
    imageView: ImageView,
    glide: RequestBuilder<Drawable>,
    @DrawableRes
    placeholder: Int? = null,
    @DrawableRes
    error: Int? = null,
    round: Boolean
) {
    if (round) {
        glide.apply { RequestOptions.circleCropTransform() }
    }

    placeholder?.let {
        glide.placeholder(it)
    }

    error?.let {
        glide.error(it)
    }

    glide
        .transition(withCrossFade(crossFadeFactory))
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
        .into(imageView)
}

fun String.stripHtml(): String = Jsoup.parse(this).text()

fun RecyclerView.moveToTop(moveToPosition: Int = 0) = smoothScrollToPosition(moveToPosition)

fun NavController.navigateSafe(
    @IdRes currentDestination: Int,
    action: NavDirections? = null,
    @IdRes destinationId: Int? = null,
    alternateCurrentDestinations: List<Int> = emptyList(),
    extras: FragmentNavigator.Extras? = null,
    navOptions: NavOptions? = null,
    args: Bundle? = null
) {
    this.currentDestination?.id?.let { id ->
        try {
            if (id == currentDestination || alternateCurrentDestinations.contains(id)) {
                if (action != null) {
                    if (extras != null) {
                        navigate(action, extras)
                    } else {
                        navigate(action)
                    }
                } else if (destinationId != null) {
                    navigate(destinationId, args, navOptions, extras)
                }
            }
        } catch (e: Exception) {
            Timber.w(e)
        }
    }
}

fun <T> MutableLiveData<T>.trigger() {
    value = value
}

fun getGooglePlayUrl(applicationId: String = BuildConfig.APPLICATION_ID) =
    "http://play.google.com/store/apps/details?id=$applicationId"
