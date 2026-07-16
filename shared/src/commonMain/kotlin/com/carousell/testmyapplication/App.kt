package com.carousell.testmyapplication

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.api.product.list.data.ProductList
import com.carousell.testmyapplication.serializers.ProductListType
import com.product.details.ProductDetailsScreen
import com.product.list.uicomponents.ProductListScreen
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource
import testmyapplication.shared.generated.resources.Res
import testmyapplication.shared.generated.resources.app_home_title
import kotlin.reflect.typeOf


@Serializable
object ListDestination

@Serializable
data class DetailDestination(val itemID: String)


@Composable
fun App() {
    MaterialTheme {
        Surface {
            val navController: NavHostController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = ListDestination
            ) {
                composable<ListDestination> {
                    ProductListScreen(
                        navigateToDetails = { itemId ->
                            navController.navigate(DetailDestination(itemId))
                        },
                        topBar = {
                            AppToolBar(
                                title = stringResource(Res.string.app_home_title),
                                onClick = {

                                }
                            )
                        }
                    )
                }
                composable<DetailDestination> { backStackEntry ->
                    ProductDetailsScreen(
                        item = backStackEntry.toRoute<DetailDestination>().itemID,
                        topBar = {
                            TopAppBar(
                                title = {},
                                navigationIcon = {
                                    IconButton(onClick = navController::popBackStack) {
                                        Icon(
                                            Icons.AutoMirrored.Filled.ArrowBack,
                                            stringResource(Res.string.app_home_title)
                                        )
                                    }
                                }
                            )
                        },
                        onClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

