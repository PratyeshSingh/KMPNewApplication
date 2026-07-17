package com.product.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.api.cache.ApiCacheHolder
import com.api.product.list.data.ProductList
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ProductDetailsViewModel(
    private val apiCacheHolder: ApiCacheHolder
) : ViewModel() {

    private val _state = MutableStateFlow<ProductList?>(null)
    @NativeCoroutines
    val state: StateFlow<ProductList?> = _state.asStateFlow()


    fun getProductDetails(itemID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = apiCacheHolder.fetchApiDetails(itemID) as ProductList?
            if (data == null) {
                _state.value = null
            } else {
                _state.value = data
            }
        }
    }
}