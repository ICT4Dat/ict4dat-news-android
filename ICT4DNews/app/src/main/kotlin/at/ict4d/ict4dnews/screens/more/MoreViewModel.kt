package at.ict4d.ict4dnews.screens.more

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import at.ict4d.ict4dnews.R

data class AppContributor(
    @DrawableRes val image: Int,
    @StringRes val name: Int,
    @StringRes val role: Int,
    @StringRes val url: Int,
)

class MoreViewModel : ViewModel() {
    val appContributors =
        listOf(
            AppContributor(
                image = R.drawable.paul,
                name = R.string.paul_spiesberger,
                role = R.string.software_developer,
                url = R.string.url_paul,
            ),
            AppContributor(
                image = R.drawable.raja,
                name = R.string.raja_saboor_ali,
                role = R.string.software_developer,
                url = R.string.url_raja,
            ),
            AppContributor(
                image = R.drawable.noah,
                name = R.string.noah_alorwu,
                role = R.string.software_developer,
                url = R.string.url_noah,
            ),
            AppContributor(
                image = R.drawable.job,
                name = R.string.job_guitiche,
                role = R.string.software_developer,
                url = R.string.url_job,
            ),
            AppContributor(
                image = R.drawable.chloe,
                name = R.string.chlo_zimmermann,
                role = R.string.designer,
                url = R.string.url_chloe,
            ),
        ).shuffled()
}
