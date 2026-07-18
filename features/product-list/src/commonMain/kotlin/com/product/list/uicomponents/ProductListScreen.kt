package com.product.list.uicomponents

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.product.list.viewmodel.ListViewModel
import com.product.list.viewmodel.ProductAction
import com.product.list.viewmodel.ProductState
import org.koin.compose.viewmodel.koinViewModel

private sealed interface ListAction {
    data class SearchAction(val keyWord: String) : ListAction

    data object ProductListAction : ListAction
}
private typealias ListActionHandler = (ListAction) -> Unit

@Composable
fun ProductListScreen(
    searchQuery: String,
    topBar: @Composable () -> Unit = {},
    navigateToDetails: (item: String) -> Unit,
    viewModel: ListViewModel = koinViewModel(),
) {
    fun actionHandler(actions: ListAction) {
        when (actions) {
            ListAction.ProductListAction -> {
                viewModel.getProductList()
            }

            is ListAction.SearchAction -> {
                viewModel.searchProduct(searchQuery)
            }
        }
    }

    Scaffold(
        topBar = topBar,
        containerColor = MaterialTheme.colorScheme.background,
    ) { innerPadding ->
        ProductListBody(
            state = viewModel.state.collectAsStateWithLifecycle().value,
            modifier = Modifier.padding(innerPadding),
            navigateToDetails = navigateToDetails,
            searchQuery = searchQuery,
            actions = ::actionHandler,
            vmActions = viewModel::actionHandler,
        )
    }
}

@Composable
private fun ProductListBody(
    state: ProductState,
    searchQuery: String,
    modifier: Modifier = Modifier,
    navigateToDetails: (item: String) -> Unit,
    actions: ListActionHandler,
    vmActions: (ProductAction) -> Unit,
) {
    var hasLoadedInitially by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(searchQuery) {
        if (searchQuery.isNotEmpty()) {
            actions(ListAction.SearchAction(searchQuery))
        } else if (hasLoadedInitially.not()) {
            actions(ListAction.ProductListAction)
            hasLoadedInitially = true
        }
    }

    when (state) {
        is ProductState.Content -> {
            ProductListScreenContent(
                modifier = modifier,
                products = state.data,
                productAction = { action ->
                    if (action is ProductAction.ViewDetail) {
                        navigateToDetails(action.item)
                    } else {
                        vmActions(action)
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
                productAction = vmActions,
            )
        }
    }
}
