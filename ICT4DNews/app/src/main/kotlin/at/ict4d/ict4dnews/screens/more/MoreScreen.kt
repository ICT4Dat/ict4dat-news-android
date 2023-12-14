package at.ict4d.ict4dnews.screens.more

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.screens.util.compose.SpacerVertical
import at.ict4d.ict4dnews.ui.theme.AppTheme

@RequiresApi(Build.VERSION_CODES.Q)
@Preview(
    name = "light-theme",
    group = "themes",
    showSystemUi = true,
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
private fun PreviewCourseDetailScreen() {
    AppTheme {
        MoreScreen(
            onRateApp = {},
            onShareApp = {},
            onContactUs = {},
            onOpenGithubProject = {}
        )
    }
}

@Composable
fun MoreScreen(
    onRateApp: () -> Unit,
    onShareApp: () -> Unit,
    onContactUs: () -> Unit,
    onOpenGithubProject: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        TextAboutDevelopers(text = stringResource(R.string.about_developers))
        SpacerVertical(height = 16.dp)

        ListOfDevelopers()
        SpacerVertical(height = 16.dp)

        Button(text = stringResource(R.string.rate_application), onClick = { onRateApp.invoke() })
        Button(text = stringResource(R.string.share_application), onClick = { onShareApp.invoke() })
        Button(text = stringResource(R.string.contact_us), onClick = { onContactUs.invoke() })
        Button(text = stringResource(R.string.github_project), onClick = { onOpenGithubProject.invoke() })
    }
}

@Composable
private fun Button(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun TextAboutDevelopers(text: String) {
    Text(
        text = text,
        textAlign = TextAlign.Justify,
        fontSize = 16.sp,
    )
}

@Composable
private fun DevProfile(
    @DrawableRes
    image: Int,
    name: String,
    role: String = stringResource(id = R.string.software_developer)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center,
            modifier = Modifier.size(72.dp)
        )
        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = role,
            fontSize = 16.sp,
        )
    }
}

private data class ICT4DDeveloper(
    val image: Int,
    val name: String,
    val role: String
)

@Composable
private fun ListOfDevelopers() {

    val ict4dDevs = listOf(
        ICT4DDeveloper(image = R.drawable.paul, name = stringResource(R.string.paul_spiesberger), role = stringResource(R.string.software_developer)),
        ICT4DDeveloper(image = R.drawable.raja, name = stringResource(R.string.raja_saboor_ali), role = stringResource(R.string.software_developer)),
        ICT4DDeveloper(image = R.drawable.noah, name = stringResource(R.string.noah_alorwu), role = stringResource(R.string.software_developer)),
        ICT4DDeveloper(image = R.drawable.job, name = stringResource(R.string.job_guitiche), role = stringResource(R.string.software_developer)),
        ICT4DDeveloper(image = R.drawable.chloe, name = stringResource(R.string.chlo_zimmermann), role = stringResource(R.string.designer))
    )
    val shuffledIct4dDevs = ict4dDevs.shuffled()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp,
            alignment = Alignment.Top
        ),
    ) {
        items(shuffledIct4dDevs) { ict4dDev ->
            DevProfile(
                image = ict4dDev.image,
                name = ict4dDev.name,
                role = ict4dDev.role
            )
        }
    }
}
