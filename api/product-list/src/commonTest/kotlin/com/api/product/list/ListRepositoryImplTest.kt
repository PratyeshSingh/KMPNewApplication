package com.api.product.list

import com.api.product.FakeApiCacheHolder
import com.api.product.FakeProductListApi
import com.api.product.list.data.ProductListResponse
import com.api.product.list.repository.ListRepositoryImpl
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ListRepositoryImplTest {

    private lateinit var fakeProductListApi: FakeProductListApi
    private lateinit var fakeApiCacheHolder: FakeApiCacheHolder
    private lateinit var repository: ListRepositoryImpl

    @BeforeTest
    fun setUp() {
        fakeProductListApi = FakeProductListApi()
        fakeApiCacheHolder = FakeApiCacheHolder()
        repository = ListRepositoryImpl(fakeProductListApi, fakeApiCacheHolder)
    }

    @Test
    fun `getList returns response and saves to cache when API call is successful`() = runTest {
        // Arrange: Prepare a mock response
        val expectedResponse = ProductListResponse(
            limit = 10,
            skip = 0,
            total = 50
        )
        fakeProductListApi.responseToReturn = expectedResponse

        // Act: Call the repository method
        val actualResponse = repository.getList()

        // Assert: Verify API was called, data was cached, and the response matches
        assertTrue(fakeProductListApi.getProductListCalled)
        assertEquals(expectedResponse, actualResponse)

        assertTrue(fakeApiCacheHolder.saveCalled)
        assertEquals("PRODUCTLIST", fakeApiCacheHolder.savedKey)
        assertEquals(expectedResponse, fakeApiCacheHolder.savedResponse)
    }

    @Test
    fun `getList returns null and does not cache when API call returns null`() = runTest {
        // Arrange: Make the API return null
        fakeProductListApi.responseToReturn = null

        // Act: Call the repository method
        val actualResponse = repository.getList()

        // Assert: Verify API was called, but nothing was saved to cache
        assertTrue(fakeProductListApi.getProductListCalled)
        assertNull(actualResponse)
        assertFalse(fakeApiCacheHolder.saveCalled)
    }

    @Test
    fun `searchProduct returns response and saves to cache when API call is successful`() = runTest {
        // Arrange: Prepare query and expected response
        val query = "phone"
        val expectedResponse = ProductListResponse(limit = 10, skip = 0, total = 5)

        // Set up your fake API (ensure your FakeProductListApi has these properties for searching)
        fakeProductListApi.responseToReturn = expectedResponse

        // Act
        val actualResponse = repository.searchProduct(query)

        // Assert
        assertTrue(fakeProductListApi.searchProductCalled) // Verify API search was triggered
        assertEquals(query, fakeProductListApi.searchQueryPassed) // Verify correct query was passed
        assertEquals(expectedResponse, actualResponse)

        // Verify Caching
        assertTrue(fakeApiCacheHolder.saveCalled)
        // Adjust cache key assertion based on how you construct the key for searches:
        assertEquals("PRODUCTLIST", fakeApiCacheHolder.savedKey)
        assertEquals(expectedResponse, fakeApiCacheHolder.savedResponse)
    }

    @Test
    fun `searchProduct returns null and does not cache when API search returns null`() = runTest {
        // Arrange
        val query = "invalid_query"
        fakeProductListApi.responseToReturn = null

        // Act
        val actualResponse = repository.searchProduct(query)

        // Assert
        assertTrue(fakeProductListApi.searchProductCalled)
        assertNull(actualResponse)
        assertFalse(fakeApiCacheHolder.saveCalled)
    }

    @Test
    fun `searchProduct returns null and does not cache when API search throws exception`() = runTest {
        // Arrange
        val query = "phone"
        fakeProductListApi.shouldSearchThrowException = true

        // Act
        val actualResponse = repository.searchProduct(query)

        // Assert
        assertNull(actualResponse)
        assertFalse(fakeApiCacheHolder.saveCalled)
    }
}