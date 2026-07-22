# High-Level Design (HLD)

The architecture is built on **Clean Architecture** principles to separate concerns, ensure testability, and decouple core business logic from platform-specific UI frameworks.

## 1. Architectural Overview

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

## 2. Directory & Module Mapping

Based on the repository structure, the responsibilities are mapped as follows:

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
