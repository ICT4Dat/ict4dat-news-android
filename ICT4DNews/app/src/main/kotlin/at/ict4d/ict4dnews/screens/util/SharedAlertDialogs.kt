package at.ict4d.ict4dnews.screens.util

import android.content.Context
import at.ict4d.ict4dnews.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showOwnershipAlertDialog(context: Context, okClicked: () -> Unit = {}) {
    val dialog = MaterialAlertDialogBuilder(context)
        .setTitle(R.string.ownership_dialog_title)
        .setMessage(R.string.ownership_dialog_message)
        .setPositiveButton(R.string.dialog_ok) { _,_ ->
            okClicked.invoke()
        }
        .setCancelable(false)

    dialog.show()
}
