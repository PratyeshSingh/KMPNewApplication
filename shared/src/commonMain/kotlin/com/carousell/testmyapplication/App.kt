package com.carousell.testmyapplication

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.product.list.uicomponents.Loader
import com.product.list.uicomponents.ProductListScreen
import com.product.list.uicomponents.FeedErrorScreen
import com.product.list.viewmodel.ListViewModel
import com.product.list.viewmodel.ProductState
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel


@Serializable
object ListDestination

@Serializable
data class DetailDestination(val objectId: Int)


@Composable
@Preview
fun App() {
    MaterialTheme {
        var showContent by remember { mutableStateOf(false) }
        val viewModel: ListViewModel = koinViewModel()

        val refreshCall = remember { mutableStateOf(true) }
        LaunchedEffect( refreshCall.value) {
            viewModel.getProductList()
        }

        val state = viewModel.state.collectAsStateWithLifecycle().value

        when (state) {
            is ProductState.Content -> {
                ProductListScreen(
                    products = state.data,
                    productAction = viewModel::actionHandler,
                )
            }
            ProductState.Loading -> {
                Loader()
            }
            ProductState.Error -> {
                FeedErrorScreen(viewModel::actionHandler)
            }
            ProductState.Refresh -> {
                refreshCall.value = !refreshCall.value
            }

        }
    }
}