pluginManagement {
    val gradleVersionsPlugin: String by settings
    val spotlessVersion: String by settings
    val gradleVersion: String by settings

    // internal
    val localConnectionEnabled: String by settings
    val artifactoryVersion: String by settings

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    plugins {
        id("com.diffplug.spotless") version spotlessVersion
        id("com.android.library") version gradleVersion
        id("com.github.ben-manes.versions") version gradleVersionsPlugin
        kotlin("android")
        kotlin("kapt")
        if (localConnectionEnabled.toBoolean()) {
            id("com.jfrog.artifactory") version artifactoryVersion
        }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://artifactory.surfstudio.ru/artifactory/libs-release-local")
    }
    versionCatalogs {
        create("libs") {
            from(fileTree("dependencies"))
        }
    }
}
rootProject.name = "surf-mvi-demo"

include(":app")
include(":components")
include(":core")
include(":debug")
include(":debug_no_op")
include(":domain")
include(":network")
include(":resources")
include(":test_utils")

include(":features:basket")
include(":features:catalog")
include(":features:feed")
include(":features:profile")
include(":features:shops")

val localConnectionEnabled: String by settings

// Local connection for libraries for debug
if (localConnectionEnabled.toBoolean()) {
    include(":mvi-core")
    project(":mvi-core").projectDir = File(settingsDir, "../surf-mvi-flow/mvi-core")

    include(":mvi-mappers")
    project(":mvi-mappers").projectDir = File(settingsDir, "../surf-mvi-flow/mvi-mappers")
}
