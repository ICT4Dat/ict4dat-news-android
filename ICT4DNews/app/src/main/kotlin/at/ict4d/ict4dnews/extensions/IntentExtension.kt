package at.ict4d.ict4dnews.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import at.ict4d.ict4dnews.BuildConfig
import at.ict4d.ict4dnews.R
import timber.log.Timber
import java.net.URLEncoder

fun FragmentActivity.safeStartActivity(intent: Intent?): Boolean {
    return try {
        startActivity(intent)
        true
    } catch (exception: ActivityNotFoundException) {
        Timber.d(exception, "No activity found to start intent.")
        Toast.makeText(this, getString(R.string.error_dialog_msg), Toast.LENGTH_LONG).show()
        false
    }
}

fun Context.safeStartActivity(intent: Intent?): Boolean {
    return try {
        startActivity(intent)
        true
    } catch (exception: ActivityNotFoundException) {
        Timber.d(exception, "No activity found to start intent.")
        false
    }
}

fun FragmentActivity.openMapsWithLocation(
    latitude: String,
    longitude: String,
    locationName: String
) {
    val stringUri =
        "geo:$latitude,$longitude?q=${URLEncoder.encode(locationName, "UTF-8")}&z=10"
    val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(stringUri))
    safeStartActivity(mapIntent)
}

fun FragmentActivity.browseCustomTabWithUrl(url: String) {
    val uri = safeParseUri(url) ?: return
    browseCustomTabWithUri(uri)
}

fun FragmentActivity.browseCustomTabWithUri(uri: Uri) {
    try {
        CustomTabsIntent
            .Builder()
            .setDefaultColorSchemeParams(
                CustomTabColorSchemeParams.Builder()
                    .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .build()
            )
            .build()
            .launchUrl(this, uri)
    } catch (e: Exception) {
        Timber.d(e, "URL couldn't be opened in custom tab, trying external browser fallback.")
        // fallback with external browser
        safeStartActivity(Intent(Intent.ACTION_VIEW, uri))
    }
}

fun safeParseUri(url: String): Uri? {
    return try {
        Uri.parse(url)
    } catch (e: Exception) {
        Timber.d(e, "URI parsing has failed for $url")
        null
    }
}

fun FragmentActivity.share(
    text: String,
    title: String = getString(R.string.share),
    subject: String = ""
): Boolean {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, text)

    return safeStartActivity(Intent.createChooser(intent, title))
}

fun FragmentActivity.email(email: String, subject: String = "", text: String = ""): Boolean {
    val intent = Intent(Intent.ACTION_SENDTO)
    intent.data = Uri.parse("mailto:")
    intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))

    if (subject.isNotEmpty()) {
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    }

    if (text.isNotEmpty()) {
        intent.putExtra(Intent.EXTRA_TEXT, text)
    }

    return safeStartActivity(intent)
}

fun FragmentActivity.openGooglePlayApp(packageName: String = BuildConfig.APPLICATION_ID) {
    val playStoreLink = getGooglePlayUrl(packageName)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(playStoreLink)
        setPackage("com.android.vending")
    }
    safeStartActivity(intent)
}

fun FragmentActivity.openNotificationSettings(): Boolean {
    val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, BuildConfig.APPLICATION_ID)
        }
    } else {
        Intent("android.settings.APP_NOTIFICATION_SETTINGS").apply {
            putExtra("app_package", BuildConfig.APPLICATION_ID)
            putExtra("app_uid", applicationInfo.uid)
        }
    }
    return safeStartActivity(intent)
}
