# KMPNewApplication: Product Browser

A cross-platform **Product Catalog Prototype** built using **Kotlin Compose Multiplatform (KMP)** targeting Android and iOS.

## 🚀 Getting Started

This project demonstrates a clean, production-ready architecture for multiplatform mobile development.

### Quick Links
- **[Requirements & Task Overview](docs/Task.md)**: The original business and technical requirements.
- **[High-Level Design (HLD)](docs/HLD.md)**: Architectural overview and module mapping.
- **[Low-Level Design (LLD)](docs/LLD.md)**: Detailed class structures, data flows, and tech stack choices.
- **[SDK & Library Details](docs/SDK_Details.md)**: Versions and descriptions of all used libraries.

---

## 🏗️ Architecture Overview

The project follows **Clean Architecture** principles to ensure testability and separation of concerns:

- **Presentation Layer**: Compose Multiplatform UI & ViewModels (StateFlow).
- **Domain Layer**: Business logic, Use Cases, and Repository interfaces.
- **Data Layer**: Ktor HTTP client, Serialization, and Repository implementations.

See the **[HLD](docs/HLD.md)** for a detailed diagram and folder structure.

---

## 🛠️ Technical Stack

- **UI**: Compose Multiplatform
- **Networking**: Ktor Client
- **Serialization**: kotlinx.serialization
- **DI**: Koin
- **Asynchronous Logic**: Kotlin Coroutines & Flow

---

## 🧪 Developer Tools & Debugging

The project includes pre-configured tools to assist with development and debugging:

### **Network Monitoring: Wiretap KMP**
Track all network requests and responses directly on the device.
- **Trigger**: **Shake the device** (or use `Cmd + M` in the Android Emulator, or use `Ctrl` + `Cmd` + `Z` on iOS Simulator) to open the Wiretap UI.
- **Features**: View headers, payloads, and response times for all Ktor requests.

### **Memory Leak Detection: LeakCanary (Android)**
Automatically monitors memory leaks in the Android application.
- **Usage**: If a leak is detected, a notification will appear. You can also open the **Leaks** app icon on the device to view detailed leak traces.

---

## 📥 Installation & Running

### Android
1. Open the project in Android Studio.
2. Select the `androidApp` run configuration.
3. Click **Run**.

### iOS
1. Open the project in Android Studio or Xcode.
2. If using Android Studio, select the `iosApp` run configuration.
3. Ensure you have an iOS Simulator or device connected.
4. Click **Run**.

*Note: For iOS, you need a Mac with Xcode installed.*
