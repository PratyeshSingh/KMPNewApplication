package com.carousell.testmyapplication.viewmodel

sealed class ProductAction {
    data class ViewDetail(val itemId: String) : ProductAction()
    data object Retry : ProductAction()
    data object Refresh : ProductAction()
}

typealias ProductActionHandler = (ProductAction) -> Unit

