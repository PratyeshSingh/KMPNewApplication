package com.carousell.testmyapplication


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import testmyapplication.shared.generated.resources.Res
import testmyapplication.shared.generated.resources.cd_more_info
import testmyapplication.shared.generated.resources.more_vert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppToolBar(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onSearchQuery: (String) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    TopAppBar(
        modifier = modifier,
        title = {
            if (isSearchActive) {
                TextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                    },
                    placeholder = { Text("Search...", color = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    // 1. Set the IME Action to Search or Done
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search // Renders a magnifying glass icon on the keyboard
                    ),
                    // 2. Handle the click action
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            // This triggers when the user clicks the search/done button on the keyboard
                            onSearchQuery(searchQuery)

                            // Optional: If you want to close the keyboard or exit search mode automatically:
                             isSearchActive = false
                        }
                    ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = MaterialTheme.colorScheme.onSecondary
                    )
                )
            } else {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        },
        navigationIcon = {
            // Navigation icon (e.g., menu or back arrow)
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Gray),
        actions = {
            if (isSearchActive) {
                // Clear / Close Search Button
                IconButton(onClick = {
                    if (searchQuery.isNotEmpty()) {
                        searchQuery = ""
                        onSearchQuery("")
                    } else {
                        isSearchActive = false
                    }
                }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Close Search",
                        tint = Color.White
                    )
                }
            } else {
                // Search Trigger Button
                IconButton(onClick = { isSearchActive = true }) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }

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
        }
    )
}