Here is the updated, highly detailed HLD + LLD document in Markdown format.

At the very top, you will find a concise executive summary of the requirements outlined in [Task.md](https://github.com/PratyeshSingh/KMPNewApplication/blob/master/Task.md) to contextualize the project for anyone reviewing this design.

---

# Architecture Design Document: Product Browser (KMP)

## Executive Summary: Task.md Requirements

The goal of this project is to build a cross-platform **Product Catalog Prototype** (Product Browser App) using **Kotlin Compose Multiplatform (KMP)** targeting Android and iOS.

* **Business Features:** A product listing view (displaying thumbnail, name, and price), a product detail view (displaying title, description, brand, price, and rating), live API-based search by keyword, and an optional category filter.
* **Technical Constraints:** Strict separation of concerns via **Clean Architecture** (Data, Domain, Presentation layers), **Ktor Client** for network operations, **kotlinx.serialization** for handling JSON, and state management using **StateFlow** inside ViewModels. The deliverable requires a clean, runnable cross-platform build, at least one unit test covering a business use case or repository, and architectural documentation.

---

## 1. High-Level Design (HLD)

The architecture is built on **Clean Architecture** principles to separate concerns, ensure testability, and decouple core business logic from platform-specific UI frameworks.

### 1.1. Architectural Overview

The project follows a uni-directional data flow (UDF) pattern spanning three primary layers:

```
┌────────────────────────────────────────────────────────┐
│                   Presentation Layer                   │
│         (Compose Multiplatform UI & ViewModels)        │
└───────────────────────────┬────────────────────────────┘
                            │ (Invokes Use Cases)
                            ▼
┌────────────────────────────────────────────────────────┐
│                      Domain Layer                      │
│            (Business Rules, Models, Use Cases)         │
└───────────────────────────┬────────────────────────────┘
                            │ (Accesses Abstractions)
                            ▼
┌────────────────────────────────────────────────────────┐
│                       Data Layer                       │
│     (Ktor Client, Repositories, Serializers, Cache)    │
└────────────────────────┘───────────────────────────────┘

```

* **Presentation Layer (`features/`, `shared/`):** Contains the Compose Multiplatform declarative UI views, screen states, and `ViewModels` managing UI states with `StateFlow`.
* **Domain Layer (`foundation/`, `shared/`):** Contains the core business logic, pristine domain models, use cases (interactors), and repository interfaces. This layer has zero dependencies on external libraries or frameworks.
* **Data Layer (`network/`, `api/`):** Responsible for data fetching, mapping remote JSON schemas to domain entities, and implementing repository interfaces using **Ktor Client** and **kotlinx.serialization**.

---

### 1.2. Directory & Module Mapping

Based on your repository structure, the responsibilities are mapped as follows:

```
├── androidApp/           # Android host application (bootstraps shared Compose)
├── iosApp/               # iOS host application (bootstraps shared UIViewController)
├── shared/               # Main multiplatform entry point
│   └── src/
│       ├── commonMain/   # Core shared UI & Logic
│       ├── androidMain/  # Android specific platform adapters
│       └── iosMain/      # iOS specific platform adapters
├── features/             # Feature-based presentation modules (List, Detail)
├── foundation/           # Core interfaces, domain base classes, DI setup
├── network/              # Ktor HTTP client wrapper and base configuration
└── api/                  # API contracts and specific model serializations

```

---

## 2. Low-Level Design (LDD)

This section drills down into the precise class structures, data flows, and serialization schemas used to implement the app's primary features.

### 2.1. Domain Layer Components

#### Entity: Product

Plain Kotlin Data Class representing the domain model used across the presentation layer.

```kotlin
package com.revest.productbrowser.domain.model

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val brand: String,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)

```

#### Use Cases

Encapsulates single business interactions.

1. **`GetProductsUseCase`**: Retrieves the list of products or optionally filters them by category.
2. **`SearchProductsUseCase`**: Searches products matching a query string via the remote API.

```kotlin
class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(category: String? = null): Result<List<Product>> {
        return repository.getProducts(category)
    }
}

class SearchProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(query: String): Result<List<Product>> {
        if (query.isBlank()) return Result.success(emptyList())
        return repository.searchProducts(query)
    }
}

```

---

### 2.2. Data Layer Components

#### Remote Data Transfer Objects (DTOs)

The schema mappings using `kotlinx.serialization` to cleanly parse payloads from the [DummyJSON Products API](https://dummyjson.com/docs/products).

```kotlin
@Serializable
data class ProductResponseDto(
    val products: List<ProductDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)

@Serializable
data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val brand: String? = "Unknown",
    val category: String,
    val thumbnail: String,
    val images: List<String>
) {
    // Mapper function to Domain Entity
    fun toDomain(): Product = Product(
        id = id,
        title = title,
        description = description,
        price = price,
        rating = rating,
        brand = brand ?: "Unknown",
        category = category,
        thumbnail = thumbnail,
        images = images
    )
}

```

#### API Service (Ktor Integration)

Using Ktor Client to build HTTP endpoints.

```kotlin
class ProductApiService(private val httpClient: HttpClient) {
    private val baseUrl = "https://dummyjson.com/products"

    suspend fun fetchProducts(category: String? = null): ProductResponseDto {
        val url = if (category.isNullOrEmpty()) baseUrl else "$baseUrl/category/$category"
        return httpClient.get(url).body()
    }

    suspend fun searchProducts(query: String): ProductResponseDto {
        return httpClient.get("$baseUrl/search") {
            parameter("q", query)
        }.body()
    }
}

```

---

### 2.3. Presentation Layer (UI State Flow)

#### UI State Definitions

The ViewModels expose state to Compose via Kotlin `StateFlow`. This prevents UI components from modifying states directly.

```kotlin
sealed interface ProductListUiState {
    object Loading : ProductListUiState
    data class Success(val products: List<Product>, val selectedCategory: String? = null) : ProductListUiState
    data class Error(val message: String) : ProductListUiState
}

```

#### Shared ViewModel Implementation

Using `StateFlow` and structured concurrency via `CoroutineScope`.

```kotlin
class ProductListViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) {
    private val _uiState = MutableStateFlow<ProductListUiState>(ProductListUiState.Loading)
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    fun loadProducts() {
        _uiState.value = ProductListUiState.Loading
        // Launch in coroutine context safe for multiplatform
    }

    fun search(query: String) {
        _uiState.value = ProductListUiState.Loading
        // Launch query matching execution logic...
    }
}

```

---

## 3. Tech Stack & Library Choices

| Responsibility | Choice | Reason |
| --- | --- | --- |
| **UI Framework** | Compose Multiplatform | Shared UI code-base across Android & iOS with native-level performance. |
| **Network Client** | Ktor Client | Fully multiplatform-first HTTP architecture designed for Kotlin Coroutines. |
| **Serialization** | `kotlinx.serialization` | Highly optimized compiler plugin, safer and lighter than reflection-based alternatives. |
| **Concurrency** | Kotlin Coroutines & Flows | Structured asynchronous model matching natural reactive architectures. |

---

## 4. Platform Bootstrap Setup

The entry point of the shared application is initialized uniquely inside each native runner:

### Android Initialization (`androidApp/`)

The Android application sets up Compose Multiplatform seamlessly in a standard `MainActivity`.

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App() // Shared entry composable from commonMain
        }
    }
}

```

### iOS Initialization (`iosApp/`)

The native Swift application maps the Compose view into a native `UIViewController`.

```swift
import SwiftUI
import shared

struct ComposeView: UIViewControllerRepresentable {
    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController() // Swift hook into commonMain entry
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

```