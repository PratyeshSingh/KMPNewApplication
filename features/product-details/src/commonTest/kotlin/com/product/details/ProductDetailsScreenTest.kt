package com.product.details

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.v2.runComposeUiTest
import com.api.product.list.data.ProductList
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ProductDetailsScreenTest : BaseTest() {
    private val fakeRepository = FakeApiCacheHolder()

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                module {
                    factory { ProductDetailsViewModel(fakeRepository) }
                },
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun whenStateIsNull_showsLoadingSpinner() =
        runComposeUiTest {
            fakeRepository.savedResponse = null

            setContent {
                ProductDetailsScreen(
                    item = "1",
                    onClick = {},
                )
            }

            onNodeWithTag("loading_indicator").assertIsDisplayed()
        }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun whenStateHasData_showsProductDetails() =
        runComposeUiTest {
            val product =
                ProductList(
                    id = 1,
                    title = "MacBook Pro",
                    brand = "Apple",
                    category = "Laptops",
                    description = "High performance laptop",
                    warrantyInformation = "1 Year",
                    thumbnail = "https://example.com/image.png",
                )
            fakeRepository.savedResponse = product

            setContent {
                ProductDetailsScreen(
                    item = "1",
                    onClick = {},
                )
            }

            onNodeWithText("MacBook Pro").assertIsDisplayed()
            onNodeWithTag("loading_indicator").assertDoesNotExist()
            val labeledInfo = "Brand: "
            onNodeWithText(labeledInfo + "Apple").assertIsDisplayed()
        }
}
