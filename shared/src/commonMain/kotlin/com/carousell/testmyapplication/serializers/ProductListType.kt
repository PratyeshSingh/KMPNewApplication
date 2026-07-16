package com.carousell.testmyapplication.serializers

import androidx.navigation.NavType
import androidx.savedstate.read
import androidx.savedstate.write
import com.api.product.list.data.ProductList
import kotlinx.serialization.json.Json


val ProductListType = object : NavType<ProductList>(isNullableAllowed = false) {
    override fun put(bundle: androidx.savedstate.SavedState, key: String, value: ProductList) {
        bundle.write {
            putString(key, Json.encodeToString(value))
        }
    }

    override fun get(bundle: androidx.savedstate.SavedState, key: String): ProductList? {
        return bundle.read {
            getString(key)?.let { Json.decodeFromString(it) }
        }
    }

    override fun parseValue(value: String): ProductList {
        return Json.decodeFromString(value)
    }

    override fun serializeAsValue(value: ProductList): String {
        return Json.encodeToString(value)
    }
}