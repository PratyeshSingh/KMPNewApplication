package com.product.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.api.cache.ApiCacheHolder
import com.api.product.list.data.ProductList
import com.api.product.list.data.ProductListResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val apiCacheHolder: ApiCacheHolder
) : ViewModel() {

    private val _state = MutableStateFlow<ProductList?>(null)
    val state = _state.asStateFlow()


    fun getProductDetails(itemID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = apiCacheHolder.fetchApiDetails("PRODUCTLIST") as ProductListResponse?

            response?.let {
                val data = response.products.find { it.id.toString() == itemID }
                if (data == null) {
                    _state.value = null
                } else {
                    _state.value = data
                }

            }
        }
    }
}