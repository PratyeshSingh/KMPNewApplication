package com.carousell.testmyapplication


import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import testmyapplication.shared.generated.resources.Res
import testmyapplication.shared.generated.resources.app_home_title
import testmyapplication.shared.generated.resources.cd_more_info
import testmyapplication.shared.generated.resources.more_vert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolBar(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSecondary
            )
        },
        navigationIcon = {
            // Navigation icon (e.g., menu or back arrow)
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Gray),
        actions = {
            Box(contentAlignment = Alignment.TopStart) {
                IconButton(onClick = {
                    expanded = !expanded
                }) {
                    Icon(
                        painter = painterResource(Res.drawable.more_vert),
                        contentDescription = stringResource(Res.string.cd_more_info),
                        tint = Color.White
                    )
                }
                FeedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    },
//                    onMenuClick = { it->
//                        expanded = it
//                    }
                )
            }
        }
    )
}