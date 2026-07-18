package com.product.list.viewmodel

sealed class ProductAction {
    data class ViewDetail(val item: String) : ProductAction()

    data object Retry : ProductAction()

    data object Reset : ProductAction()
}

typealias ProductActionHandler = (ProductAction) -> Unit
