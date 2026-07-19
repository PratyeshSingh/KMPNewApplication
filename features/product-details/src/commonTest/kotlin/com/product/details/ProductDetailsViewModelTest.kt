package com.product.details

import app.cash.turbine.test
import com.api.product.list.data.ProductList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class ProductDetailsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var fakeRepository: FakeApiCacheHolder

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeRepository = FakeApiCacheHolder()
        viewModel = ProductDetailsViewModel(fakeRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getProductDetails_emitsNullThenProductData() =
        runTest {
            // Arrange
            val expectedProduct =
                ProductList(
                    id = 1,
                    title = "MacBook Pro",
                    brand = "Apple",
                    category = "Laptops",
                    description = "High performance laptop",
                    warrantyInformation = "1 Year",
                    thumbnail = "https://example.com/image.png",
                )
            fakeRepository.saveApiDetails("1", expectedProduct)

            // Act & Assert using Turbine
            viewModel.state.test {
                // 1. Initial state should be null
                assertNull(awaitItem())

                // 2. Trigger the fetch
                viewModel.getProductDetails("1")

                // 3. Next emitted item should be the expected product
                assertEquals(expectedProduct.title, awaitItem()?.title)
            }
        }
}
