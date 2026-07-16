package com.carousell.testmyapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.product.list.uicomponents.FeedErrorScreen
import com.product.list.uicomponents.Loader
import com.product.list.uicomponents.ProductListScreen
import com.product.list.viewmodel.ListViewModel
import com.product.list.viewmodel.ProductState
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel


@Serializable
object ListDestination

@Serializable
data class DetailDestination(val objectId: Int)


@Composable
fun App() {
    MaterialTheme {
        Scaffold(
            topBar = {
                AppToolBar(
                    onClick = {
                    }
                )
            },
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            BodyContent(Modifier.padding(innerPadding))
        }
    }
}


@Composable
private fun BodyContent(
    modifier: Modifier = Modifier
) {
    val viewModel: ListViewModel = koinViewModel()

    val refreshCall = remember { mutableStateOf(true) }
    LaunchedEffect(refreshCall.value) {
        viewModel.getProductList()
    }

    val state = viewModel.state.collectAsStateWithLifecycle().value

    when (state) {
        is ProductState.Content -> {
            ProductListScreen(
                modifier = modifier,
                products = state.data,
                productAction = viewModel::actionHandler,
            )
        }

        ProductState.Loading -> {
            Loader(modifier = modifier)
        }

        ProductState.Error -> {
            FeedErrorScreen(
                modifier = modifier,
                productAction = viewModel::actionHandler
            )
        }

        ProductState.Refresh -> {
            refreshCall.value = !refreshCall.value
        }

    }
}