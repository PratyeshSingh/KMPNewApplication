import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.kotlinxSerialization)
//    alias(libs.plugins.ksp)
    alias(libs.plugins.kmpNativeCoroutines)
}

compose.resources {
    // 1. Force the generator to make the 'Res' class public so other modules can read it
    publicResClass = true

    // 2. Assign a distinct, clean package name to isolate this module's assets
    packageOfResClass = "com.product.details"

    // 3. Ensure code generation always runs for multi-module isolation
    generateResClass = always
}

kotlin {

    // Define targets (match your shared module)
    iosArm64()
    iosSimulatorArm64()


    android {
        namespace = "com.product.details"
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

            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            api(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)


            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)

            implementation(libs.ktor.serialization.kotlinx.json)

//            implementation(projects.network.ktorCore)
//            implementation(projects.foundation.logger)
            implementation(projects.api.productList)
            implementation(projects.foundation.apiCache)

            implementation(libs.koin.core)
            implementation(libs.koin.androidx.compose)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}