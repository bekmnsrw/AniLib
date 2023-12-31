package com.bekmnsrw.core.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.bekmnsrw.core.designsystem.icon.AniLibIcons
import com.bekmnsrw.core.designsystem.theme.AniLibTypography
import kotlinx.collections.immutable.persistentListOf

private val statuses = persistentListOf(
    UserRatesEnum.NOT_IN_MY_LIST,
    UserRatesEnum.PLANNED,
    UserRatesEnum.COMPLETED,
    UserRatesEnum.WATCHING,
    UserRatesEnum.DROPPED,
    UserRatesEnum.ON_HOLD
)

@Composable
fun AniLibAnimeStatusDialog(
    id: Int?,
    currentStatus: String?,
    onDismissRequest: () -> Unit,
    onRadioButtonClick: (String, Int?) -> Unit
) {
    val selectedValue = remember { mutableStateOf(currentStatus) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        title = { Text(text = stringResource(id = R.string.choose_anime_status)) },
        text = {
            Column {
                LazyColumn {
                    items(items = statuses) { item ->
                        AniLibIconToggleButton(
                            id = id,
                            isSelected = selectedValue.value == item.key,
                            selectedValue = selectedValue,
                            item = item,
                            onClick = onRadioButtonClick
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun AniLibIconToggleButton(
    id: Int?,
    isSelected: Boolean,
    selectedValue: MutableState<String?>,
    item: UserRatesEnum,
    onClick: (String, Int?) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                role = Role.RadioButton,
                onClick = {
                    if (!isSelected) {
                        selectedValue.value = item.key
                        onClick(item.key, id)
                    }
                }
            )
    ) {
        IconToggleButton(
            checked = isSelected,
            onCheckedChange = {
                if (!isSelected) {
                    selectedValue.value = item.key
                    onClick(item.key, id)
                }
            }
        ) {
            Icon(
                imageVector = when (isSelected) {
                    true -> AniLibIcons.RadioButtonChecked
                    false -> AniLibIcons.RadioButtonUnchecked
                },
                tint = when (isSelected) {
                    true -> MaterialTheme.colorScheme.primary
                    false -> MaterialTheme.colorScheme.onSurface
                },
                contentDescription = null
            )
        }
        Text(
            text = item.value,
            style = AniLibTypography.bodyLarge
        )
    }
}
