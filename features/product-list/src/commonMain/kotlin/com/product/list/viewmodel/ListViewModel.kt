package com.product.list.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.api.product.list.data.ProductList
import com.api.product.list.repository.ListRepository
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

sealed class ProductState {
    data object Loading : ProductState()

    data object Error : ProductState()

    class Content(val data: List<ProductList>) : ProductState()
}

@KoinViewModel
class ListViewModel(
    private val listRepository: ListRepository,
) : ViewModel() {
    private var mSearchText: String = ""
    private val _state = MutableStateFlow<ProductState>(ProductState.Loading)

    @NativeCoroutines
    val state: StateFlow<ProductState> = _state.asStateFlow()

    fun getProductList() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = ProductState.Loading
            val data = listRepository.getList()
            if (data == null) {
                _state.value = ProductState.Error
            } else {
                _state.value = ProductState.Content(data.products)
            }
        }
    }

    fun actionHandler(action: ProductAction) {
        when (action) {
            ProductAction.Retry, ProductAction.Reset -> getProductList()
            else -> {}
        }
    }

    fun searchProduct(searchQuery: String) {
        if (searchQuery.trim().isNotEmpty() && searchQuery != mSearchText) {
            mSearchText = searchQuery
            viewModelScope.launch(Dispatchers.IO) {
                _state.value = ProductState.Loading
                val data = listRepository.searchProduct(searchQuery)
                if (data == null) {
                    _state.value = ProductState.Error
                } else {
                    _state.value = ProductState.Content(data.products)
                }
            }
        }
    }
}
