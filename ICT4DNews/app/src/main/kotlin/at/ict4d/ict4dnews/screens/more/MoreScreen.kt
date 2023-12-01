package at.ict4d.ict4dnews.screens.more

import android.content.res.Configuration
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.ict4d.ict4dnews.R
import at.ict4d.ict4dnews.ui.theme.AppTheme
import com.google.android.material.color.MaterialColors

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
        MoreScreen()
    }
}

@Composable
private fun MoreScreen() {
    Column {
        Text(
            text = stringResource(id = R.string.about_developers),
            modifier = Modifier.padding(horizontal = 8.dp),
            style = TextStyle(
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
            )
        )

        MoreButton(text = "Bla Bla Bla...", onClick = {})

        MoreButton(text = "Bla Bla Bla...", onClick = {})

        MoreButton(text = "Bla Bla Bla...", onClick = {})

        MoreButton(text = "Bla Bla Bla...", onClick = {})
    }
}

@Composable
private fun MoreButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 4.dp,
                horizontal = 16.dp
            )
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}
