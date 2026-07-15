package com.api.product.list.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
data class ProductListResponse(
    val limit: Long,
    val products: List<ProductList> = emptyList(),
    val skip: Long,
    val total: Long
)

@Serializable
data class ProductList(
    @JsonNames(names = ["title"]) val title: String,
    @JsonNames(names = ["thumbnail"]) val thumbnail: String,
    @JsonNames(names = ["description"]) val description: String,
    @JsonNames(names = ["id"]) val id: Long
)