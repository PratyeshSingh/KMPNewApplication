package com.product.list.uicomponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.product.list.viewmodel.ListViewModel
import com.product.list.viewmodel.ProductAction
import com.product.list.viewmodel.ProductState
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun ProductListScreen(
    topBar: @Composable () -> Unit = {},
    navigateToDetails: (item: String) -> Unit
) {
    Scaffold(
        topBar = topBar,
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        ProductListBody(
            modifier = Modifier.padding(innerPadding),
            navigateToDetails = navigateToDetails
        )
    }
}

@Composable
private fun ProductListBody(
    modifier: Modifier = Modifier,
    navigateToDetails: (item: String) -> Unit
) {
    val viewModel: ListViewModel = koinViewModel()

    val refreshCall = remember { mutableStateOf(true) }
    LaunchedEffect(refreshCall.value) {
        viewModel.getProductList()
    }

    val state = viewModel.state.collectAsStateWithLifecycle().value

    when (state) {
        is ProductState.Content -> {
            ProductListScreenContent(
                modifier = modifier,
                products = state.data,
                productAction = { action ->
                    if (action is ProductAction.ViewDetail) {
                        navigateToDetails(action.item)
                    } else {
                        viewModel.actionHandler(action)
                    }
                },
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