package at.ict4d.ict4dnews.utils

import android.content.ComponentName
import android.content.Context
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession

/**
 * Manager to create a Custom Tab Session
 *
 * Use to create a session to be set when opening a custom tab.
 * Currently used to override the default behaviour:
 *
 * "Passing a session to a CustomTabIntent will force
 * open the link in a Custom Tab, even if the corresponding native app is installed."
 *
 * See more here: https://developer.chrome.com/docs/android/custom-tabs/guide-warmup-prefetch/
 */
class CustomTabSessionManager {
    private var client: CustomTabsClient? = null
    var session: CustomTabsSession? = null

    private val mConnection: CustomTabsServiceConnection =
        object : CustomTabsServiceConnection() {
            override fun onCustomTabsServiceConnected(
                name: ComponentName,
                client: CustomTabsClient,
            ) {
                // Warm up the browser process
                client.warmup(0) // placeholder for future use
                // Create a new browser session
                session = client.newSession(CustomTabsCallback())
                this@CustomTabSessionManager.client = client
            }

            override fun onServiceDisconnected(name: ComponentName) {
                client = null
                session = null
            }
        }

    fun bindCustomTabService(context: Context) {
        // Check for an existing connection
        if (client != null) {
            // Do nothing if there is an existing service connection
            return
        }

        // Get the default browser package name, this will be null if
        // the default browser does not provide a CustomTabsService
        val packageName =
            CustomTabsClient.getPackageName(
                context,
                null,
            )
                ?: // Do nothing as service connection is not supported
                return
        CustomTabsClient.bindCustomTabsService(context, packageName, mConnection)
    }
}
