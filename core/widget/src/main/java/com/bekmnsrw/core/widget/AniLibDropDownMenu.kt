package com.bekmnsrw.core.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import com.bekmnsrw.core.designsystem.icon.AniLibIcons

@Composable
fun AniLibDropDownMenu(
    offset: DpOffset,
    isDropDownMenuExpanded: Boolean,
    onDismissRequest: () -> Unit,
    onDropDownMenuItemClicked: (String) -> Unit,
    items: Map<String, String>,
    selectedItem: String
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.TopEnd)
    ) {
        DropdownMenu(
            expanded = isDropDownMenuExpanded,
            onDismissRequest = { onDismissRequest() },
            offset = offset
        ) {
            Divider()
            items.forEach { item ->
                AniLibDropDownMenuItem(
                    text = item.key,
                    filter = item.value,
                    selectedItem = selectedItem,
                    onDropDownMenuItemClicked = { onDropDownMenuItemClicked(item.value) }
                )
            }
        }
    }
}

@Composable
private fun AniLibDropDownMenuItem(
    text: String,
    filter: String,
    selectedItem: String,
    onDropDownMenuItemClicked: (String) -> Unit
) {
    DropdownMenuItem(
        onClick = { onDropDownMenuItemClicked(filter) },
        text = {
            AniLibDropDownMenuItemText(
                text = text,
                selectedItem = selectedItem,
                filter = filter
            )
        }
    )
    Divider()
}

@Composable
private fun AniLibDropDownMenuItemText(
    text: String,
    selectedItem: String,
    filter: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text)

        if (selectedItem == filter) {
            Icon(
                imageVector = AniLibIcons.CheckMark,
                contentDescription = null
            )
        }
    }
}
