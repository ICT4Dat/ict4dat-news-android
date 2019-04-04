package at.ict4d.ict4dnews.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.utils.GlideApp
import at.ict4d.ict4dnews.utils.GlideRequest
import at.ict4d.ict4dnews.utils.RxEventBus
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.simplecityapps.recyclerview_fastscroll.interfaces.OnFastScrollStateChangeListener
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import org.jsoup.Jsoup
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import java.util.Locale

fun Context.browseCustomTab(url: String?) {
    url?.let {
        CustomTabsIntent
            .Builder()
            .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .build()
            .launchUrl(this, Uri.parse(it))
    }
}

fun LocalDateTime.extractDate(): String = this.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault()))

fun String.toLocalDateTimeFromRFCString(): LocalDateTime? =
    LocalDateTime.parse(this, DateTimeFormatter.RFC_1123_DATE_TIME) ?: null

@BindingAdapter(value = ["loadFromRes", "placeholder", "error", "round"], requireAll = false)
fun ImageView.loadFromURL(
    @DrawableRes
    drawableRes: Int,
    @DrawableRes
    placeholder: Int = R.drawable.ic_refresh_black_24dp,
    @DrawableRes
    error: Int = R.drawable.ic_broken_image_black_24dp,
    round: Boolean = false
) = setUpGlideAndLoad(this, GlideApp.with(context).load(drawableRes), placeholder, error, round)

@BindingAdapter(value = ["loadFromURL", "placeholder", "error", "round"], requireAll = false)
fun ImageView.loadFromURL(
    url: String?,
    @DrawableRes
    placeholder: Int = R.drawable.ic_refresh_black_24dp,
    @DrawableRes
    error: Int = R.drawable.ic_broken_image_black_24dp,
    round: Boolean = false
) = setUpGlideAndLoad(this, GlideApp.with(context).load(url), placeholder, error, round)

private fun setUpGlideAndLoad(
    imageView: ImageView,
    glide: GlideRequest<Drawable>,
    @DrawableRes
    placeholder: Int = R.drawable.ic_refresh_black_24dp,
    @DrawableRes
    error: Int = R.drawable.ic_broken_image_black_24dp,
    round: Boolean
) {
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
        .into(imageView)
}

fun View.visible(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun String.stripHtml(): String = Jsoup.parse(this).text()

fun RecyclerView.moveToTop(moveToPosition: Int = 0) = smoothScrollToPosition(moveToPosition)

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

inline fun <reified T> RxEventBus.filterObservableAndSetThread(
    observeThread: Scheduler = AndroidSchedulers.mainThread(),
    subscribeThread: Scheduler = AndroidSchedulers.mainThread()
): Observable<T> = filteredObservable(T::class.java).observeOn(observeThread).subscribeOn(subscribeThread)

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

@BindingAdapter("toggleSwipeRefreshOnFastScroll")
fun setRecyclerViewFastScrollListener(
    fastScrollRecyclerView: FastScrollRecyclerView,
    swipeRefreshLayout: SwipeRefreshLayout
) {
    fastScrollRecyclerView.setOnFastScrollStateChangeListener(object : OnFastScrollStateChangeListener {
        override fun onFastScrollStop() {
            swipeRefreshLayout.isEnabled = true
        }

        override fun onFastScrollStart() {
            swipeRefreshLayout.isEnabled = false
        }
    })
}