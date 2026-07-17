package com.carousell.testmyapplication

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.stringResource
import kmpnewapplication.shared.generated.resources.Res
import kmpnewapplication.shared.generated.resources.popular
import kmpnewapplication.shared.generated.resources.recent

@Composable
fun FeedDropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
//    onMenuClick: (Boolean, FeedAction) -> Unit,
) {
    DropdownMenu(
        modifier = modifier,
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            onClick = {
//                onMenuClick(false, FeedAction.Recent)
            },
            text = {
                Text(text = stringResource(Res.string.recent))
            }
        )
        DropdownMenuItem(
            onClick = {
//                onMenuClick(false, FeedAction.Popular)
            },
            text = {
                Text(text = stringResource(Res.string.popular))
            }
        )
    }
}