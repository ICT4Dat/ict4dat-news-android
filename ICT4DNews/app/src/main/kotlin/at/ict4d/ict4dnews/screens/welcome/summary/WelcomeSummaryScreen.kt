package at.ict4d.ict4dnews.screens.welcome.summary

import android.Manifest
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.extensions.openNotificationSettings
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@Composable
fun WelcomeSummaryScreen(
    viewModel: WelcomeSummaryViewModel,
    onNavigateToBlogsAndSources: () -> Unit,
    onPopBackToStart: () -> Unit,
) {
    val blogsCount by viewModel.blogsCount.collectAsStateWithLifecycle(initialValue = 0)

    NotificationRuntimePermissionContainer(
        blogsCount = blogsCount,
        model = viewModel,
        onNavigateToBlogsAndSources = onNavigateToBlogsAndSources,
        onPopBackToStart = onPopBackToStart,
    )
}

@Composable
private fun SummaryScreen(
    blogsCount: Int,
    runtimeNotificationButtonText: String,
    onRuntimeNotificationButtonClicked: () -> Unit,
    onNavigateToBlogsAndSources: () -> Unit,
    onPopBackToStart: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            stringResource(id = R.string.welcome_summary_welcome),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Text(
            text = stringResource(R.string.welcome_summary_explaination, blogsCount),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onRuntimeNotificationButtonClicked,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(runtimeNotificationButtonText)
        }

        OutlinedButton(
            onClick = onPopBackToStart,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(id = R.string.welcome_summary_skip))
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { onNavigateToBlogsAndSources() },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(stringResource(id = R.string.welcome_summary_configure_sources))
        }

        Text(
            text = stringResource(id = R.string.welcome_summary_disclaimer),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationRuntimePermissionContainer(
    blogsCount: Int,
    model: WelcomeSummaryViewModel,
    onNavigateToBlogsAndSources: () -> Unit,
    onPopBackToStart: () -> Unit,
) {
    val context = LocalContext.current

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        if (notificationPermissionState.status.isGranted) {
            WelcomeDoneScreen {
                model.isAutomaticNewsUpdateEnabled.set(true)
                onPopBackToStart()
            }
        } else {
            Column {
                if (notificationPermissionState.status.shouldShowRationale) {
                    SummaryScreen(
                        blogsCount = blogsCount,
                        runtimeNotificationButtonText = stringResource(id = R.string.runtime_permission_notification_not_granted),
                        onRuntimeNotificationButtonClicked = { (context as? FragmentActivity)?.openNotificationSettings() },
                        onNavigateToBlogsAndSources = onNavigateToBlogsAndSources,
                        onPopBackToStart = onPopBackToStart,
                    )
                } else {
                    SummaryScreen(
                        blogsCount = blogsCount,
                        runtimeNotificationButtonText = stringResource(id = R.string.welcome_summary_setup),
                        onRuntimeNotificationButtonClicked = { notificationPermissionState.launchPermissionRequest() },
                        onNavigateToBlogsAndSources = onNavigateToBlogsAndSources,
                        onPopBackToStart = onPopBackToStart,
                    )
                }
            }
        }
    } else { // No runtime permission needed
        SummaryScreen(
            blogsCount = blogsCount,
            runtimeNotificationButtonText = stringResource(id = R.string.welcome_summary_setup),
            onRuntimeNotificationButtonClicked = {
                model.isAutomaticNewsUpdateEnabled.set(true)
                onPopBackToStart()
            },
            onNavigateToBlogsAndSources = onNavigateToBlogsAndSources,
            onPopBackToStart = onPopBackToStart,
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES,
    // Runtime Permission is ignored
    apiLevel = 30,
)
@Composable
fun PreviewWelcomeSummaryScreen() {
    SummaryScreen(
        blogsCount = 12,
        runtimeNotificationButtonText = "Button",
        onRuntimeNotificationButtonClicked = {},
        onNavigateToBlogsAndSources = {},
        onPopBackToStart = {},
    )
}
