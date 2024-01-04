package com.bekmnsrw.core.widget.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import com.bekmnsrw.core.widget.button.AniLibTextButton
import com.bekmnsrw.core.widget.R

@Composable
fun AniLibAlertDialog(
    onDismissRequest: () -> Unit,
    text: String,
    onConfirmButtonClick: () -> Unit,
    onDismissButtonClick: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        text = {
            Text(
                text = text,
                style = AniLibTypography.headlineSmall
            )
        },
        confirmButton = {
            AniLibTextButton(
                text = stringResource(id = R.string.confirm),
                onClick = onConfirmButtonClick
            )
        },
        dismissButton = {
            AniLibTextButton(
                text = stringResource(id = R.string.dismiss),
                onClick = onDismissButtonClick
            )
        }
    )
}
