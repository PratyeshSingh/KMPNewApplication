rootProject.name = "KMPNewApplication"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

fun include(
    name: String,
    filePath: String? = null,
) {
    settings.include(name)
    val project = project(name)
    project.configureProjectDir(filePath)
    project.configureBuildFileName(name)
}

fun ProjectDescriptor.configureProjectDir(filePath: String? = null) {
    if (filePath != null) {
        projectDir = File(rootDir, filePath)
    }
    if (!projectDir.exists()) {
        throw GradleException("Path : $projectDir does not exist, Cannot include project: $name")
    }
    if (!projectDir.isDirectory) {
        throw GradleException("Path : $projectDir is a file instead of a directory.Cannot include project: $name")
    }
}

fun ProjectDescriptor.configureBuildFileName(projectName: String) {
    val name = projectName.substringAfterLast(":")
    buildFileName = "$name.gradle.kts"
    if (!buildFile.exists()) {
        throw GradleException("Build file: $name.gradle.kts does not exist.Cannot include project: $name")
    }
}

include(":androidApp")
include(":shared")
include(":network:ktor-core")

include(":features:product-details")
include(":features:product-list")

include(":foundation:logger")
include(":foundation:host-url")
include(":foundation:api-cache")
include(":foundation:preferences")

include(":api:product-list")
