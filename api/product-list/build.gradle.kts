import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary) // or androidLibrary


    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.kmpNativeCoroutines)
}

kotlin {

    // Define targets (match your shared module)
    iosArm64()
    iosSimulatorArm64()

    androidLibrary {
        namespace = "com.api.product.list"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_22
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        commonMain.dependencies {

            implementation(libs.kotlinx.serialization.core)

            implementation(projects.network.ktorCore)
            implementation(projects.foundation.logger)

            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(projects.foundation.apiCache)

            implementation(libs.koin.androidx.compose)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}