package at.ict4d.ict4dnews.screens.more

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.ui.theme.AppTheme

@Preview
@Composable
private fun PreviewCourseDetailScreen() {
    AppTheme {
        MoreScreen(
            onRateApp = {},
            onShareApp = {},
            onContactUs = {},
            onOpenUrl = {},
            onMenuSettingsSelected = {},
            onOpenGithubProject = {}
        )
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MoreScreen(
    onRateApp: () -> Unit,
    onShareApp: () -> Unit,
    onContactUs: () -> Unit,
    onOpenUrl: (Int) -> Unit,
    onMenuSettingsSelected: () -> Unit,
    onOpenGithubProject: () -> Unit,
) {
    Scaffold(
        topBar = { AppBar(onMenuSettingsSelected = { onMenuSettingsSelected.invoke() }) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                TextAboutContributors(text = stringResource(R.string.about_developers))
                Spacer(modifier = Modifier.height(16.dp))

                ListOfContributors(onOpenUrl = onOpenUrl)
                Spacer(modifier = Modifier.height(16.dp))

                Button(text = stringResource(R.string.rate_application), onClick = onRateApp)
                Button(text = stringResource(R.string.share_application), onClick = onShareApp)
                Button(text = stringResource(R.string.contact_us), onClick = onContactUs)
                Button(text = stringResource(R.string.github_project), onClick = onOpenGithubProject)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(onMenuSettingsSelected: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.headline_more, String(Character.toChars(0x2764))),
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            IconButton(onClick = { onMenuSettingsSelected.invoke() }) {
                Icon(
                    imageVector = Icons.Rounded.Settings,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    )
}

@Composable
private fun Button(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 2.dp),
    ) {
        Text(
            text = text, fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TextAboutContributors(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp),
        textAlign = TextAlign.Justify,
        fontSize = 16.sp,
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ListOfContributors(
    onOpenUrl: (Int) -> Unit
) {
    val appContributors = listOf(
        AppContributor(
            image = R.drawable.paul,
            name = stringResource(R.string.paul_spiesberger),
            role = stringResource(R.string.software_developer),
            url = R.string.url_paul
        ),
        AppContributor(
            image = R.drawable.raja,
            name = stringResource(R.string.raja_saboor_ali),
            role = stringResource(R.string.software_developer),
            url = R.string.url_raja
        ),
        AppContributor(
            image = R.drawable.noah,
            name = stringResource(R.string.noah_alorwu),
            role = stringResource(R.string.software_developer),
            url = R.string.url_noah
        ),
        AppContributor(
            image = R.drawable.job,
            name = stringResource(R.string.job_guitiche),
            role = stringResource(R.string.software_developer),
            url = R.string.url_job
        ),
        AppContributor(
            image = R.drawable.chloe,
            name = stringResource(R.string.chlo_zimmermann),
            role = stringResource(R.string.designer),
            url = R.string.url_chloe
        ),
    ).shuffled()

    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        for (contributor in appContributors)
            ContributorProfile(
                contributor = contributor,
                onClick = {
                    onOpenUrl.invoke(contributor.url)
                }
            )
    }
}

@Composable
private fun ContributorProfile(
    contributor: AppContributor,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable { onClick.invoke() }
    ) {
        Image(
            painter = painterResource(contributor.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier.size(72.dp)
        )
        Text(
            text = contributor.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = contributor.role,
            fontSize = 16.sp,
        )
    }
}

private data class AppContributor(
    val image: Int,
    val name: String,
    val role: String,
    val url: Int
)
