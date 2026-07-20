package com.api.product.list.data

import com.carousell.testmyapplication.network.ktor.core.serialization.BooleanIntSerializer
import com.carousell.testmyapplication.network.ktor.core.serialization.InstantIso8601Serializer
import com.carousell.testmyapplication.network.ktor.core.serialization.InstantLongSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import kotlin.time.Instant

@Serializable
data class ProductListResponse(
    val limit: Long,
    val products: List<ProductList> = emptyList(),
    val skip: Long,
    val total: Long,
)

@Serializable
data class ProductList(
    @JsonNames(names = ["title"]) val title: String,
    @JsonNames(names = ["thumbnail"]) val thumbnail: String,
    @JsonNames(names = ["description"]) val description: String? = null,
    @JsonNames(names = ["warrantyInformation"]) val warrantyInformation: String? = null,
    @JsonNames(names = ["brand"]) val brand: String? = null,
    @JsonNames(names = ["category"]) val category: String? = null,
    @JsonNames(names = ["id"]) val id: Long,
)

@Serializable
data class User(
    val id: Int,
    // API sends 1 or 0
    @Serializable(with = BooleanIntSerializer::class)
    val isActive: Boolean,
)

@Serializable
data class Product(
    val id: String,
    @Serializable(with = InstantIso8601Serializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantLongSerializer::class)
    val updatedAt: Instant,
)
