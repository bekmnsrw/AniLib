package com.bekmnsrw.core.widget.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bekmnsrw.core.widget.button.AniLibTextButton
import com.bekmnsrw.core.widget.R

@Composable
fun AniLibAuthDialog(
    onDismissRequest: () -> Unit,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            AniLibTextButton(
                text = stringResource(id = R.string.auth_dialog_confirm_btn_text),
                onClick = onConfirmButtonClick
            )
        },
        dismissButton = {
            AniLibTextButton(
                text = stringResource(id = R.string.auth_dialog_dismiss_btn_text),
                onClick = onDismissButtonClick
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.auth_dialog_title)
            )
        },
        text = {
            Text(
                text = stringResource(id = R.string.auth_dialog_description)
            )
        }
    )
}
