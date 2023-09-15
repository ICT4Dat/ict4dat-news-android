package at.ict4d.ict4dnews.screens.welcome.summary

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import at.ict4d.ict4dnews.R

@Composable
fun WelcomeDoneScreen(
    onPopFragment: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = stringResource(id = R.string.welcome_summary_done_headline),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.welcome_summary_done_text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onPopFragment,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Lets go")
        }
    }
}

@Preview(
    showSystemUi = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun PreviewWelcomeDoneScreen() {
    WelcomeDoneScreen(
        onPopFragment = {}
    )
}
