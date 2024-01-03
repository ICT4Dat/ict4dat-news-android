package at.ict4d.ict4dnews.screens.more

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
            onOpenDevUrl = {},
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
    onOpenDevUrl: (Int) -> Unit,
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
                TextAboutDevelopers(text = stringResource(R.string.about_developers))
                Spacer(modifier = Modifier.height(16.dp))

                ListOfDevelopers(onOpenDevUrl = onOpenDevUrl)

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
private fun TextAboutDevelopers(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 16.dp),
        textAlign = TextAlign.Justify,
        fontSize = 16.sp,
    )
}

@Composable
private fun DeveloperProfile(
    developer: ICT4DDeveloper,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick.invoke() }
    ) {
        Image(
            painter = painterResource(developer.image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier.size(72.dp)
        )
        Text(
            text = developer.name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = developer.role,
            fontSize = 16.sp,
        )
    }
}

private data class ICT4DDeveloper(
    val image: Int,
    val name: String,
    val role: String,
    val url: Int
)

@Composable
private fun ListOfDevelopers(onOpenDevUrl: (Int) -> Unit) {

    val ict4dDevs = listOf(
        ICT4DDeveloper(
            image = R.drawable.paul,
            name = stringResource(R.string.paul_spiesberger),
            role = stringResource(R.string.software_developer),
            url = R.string.url_paul
        ),
        ICT4DDeveloper(
            image = R.drawable.raja,
            name = stringResource(R.string.raja_saboor_ali),
            role = stringResource(R.string.software_developer),
            url = R.string.url_raja
        ),
        ICT4DDeveloper(
            image = R.drawable.noah,
            name = stringResource(R.string.noah_alorwu),
            role = stringResource(R.string.software_developer),
            url = R.string.url_noah
        ),
        ICT4DDeveloper(
            image = R.drawable.job,
            name = stringResource(R.string.job_guitiche),
            role = stringResource(R.string.software_developer),
            url = R.string.url_job
        ),
        ICT4DDeveloper(
            image = R.drawable.chloe,
            name = stringResource(R.string.chlo_zimmermann),
            role = stringResource(R.string.designer),
            url = R.string.url_chloe
        ),
    )
    val shuffledIct4dDevs = ict4dDevs.shuffled()

    DevProfilesRow(
        dev1 = shuffledIct4dDevs[0],
        dev2 = shuffledIct4dDevs[1],
        onOpenDevUrl = onOpenDevUrl
    )
    Spacer(modifier = Modifier.height(16.dp))

    DevProfilesRow(
        dev1 = shuffledIct4dDevs[2],
        dev2 = shuffledIct4dDevs[3],
        onOpenDevUrl = onOpenDevUrl
    )
    Spacer(modifier = Modifier.height(16.dp))

    DevProfilesRow(
        dev1 = shuffledIct4dDevs[4],
        onOpenDevUrl = onOpenDevUrl
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun DevProfilesRow(
    dev1: ICT4DDeveloper,
    dev2: ICT4DDeveloper? = null,
    onOpenDevUrl: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        DeveloperProfile(
            developer = dev1,
            onClick = { onOpenDevUrl.invoke(dev1.url) }
        )
        Spacer(modifier = Modifier.width(16.dp))

        dev2?.let {
            DeveloperProfile(
                developer = it,
                onClick = { onOpenDevUrl.invoke(it.url) }
            )
        }
    }
}
