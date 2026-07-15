package com.carousell.testmyapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carousell.testmyapplication.data.ProductList
import com.carousell.testmyapplication.repository.ListRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


sealed class ProductState {
    data object Refresh : ProductState()
    data object Loading : ProductState()
    data object Error : ProductState()
    class Content(val data: List<ProductList>) : ProductState()
}

class ListViewModel(
    private val listRepository: ListRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<ProductState>(ProductState.Loading)
    val state = _state.asStateFlow()

    fun getProductList() {
        viewModelScope.launch {
            _state.value = ProductState.Loading
            val data = listRepository.getList()
            if (data == null) {
                _state.value = ProductState.Error
            } else {
                _state.value = ProductState.Content(data.products)
            }
        }
    }

    internal fun actionHandler(action: ProductAction) {
        when (action) {
            is ProductAction.ViewDetail -> {
                // todo
            }

            ProductAction.Retry -> getProductList()
            ProductAction.Refresh -> _state.value = ProductState.Refresh
        }
    }
}