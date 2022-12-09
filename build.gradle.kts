import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {

    val kotlinVersion: String by project
    val gradleVersion: String by project
    val hiltCoreVersion: String by project
    val googleServicesVersion: String by project
    val crashlyticsPluginVersion: String by project

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:$gradleVersion")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltCoreVersion")
        classpath("com.google.gms:google-services:$googleServicesVersion")
        classpath("com.google.firebase:firebase-crashlytics-gradle:$crashlyticsPluginVersion")
    }
}

// FIXME: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.diffplug.spotless")
    id("com.github.ben-manes.versions")
    kotlin("plugin.serialization") version "1.6.21"
    alias(libs.plugins.ksp.plugin)
}

/**
 * Run ./gradlew dependencyUpdates to check for new updates
 * in dependencies used.
 * More info at: https://github.com/ben-manes/gradle-versions-plugin
 */
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        // disallow release candidates as upgradable versions from stable versions
        isNonStable(candidate.version) && !isNonStable(currentVersion) ||
                // https://github.com/ben-manes/gradle-versions-plugin/issues/534#issuecomment-897236772
                candidate.group == "org.jacoco"
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    spotless {
        kotlin {
            target("**/*.kt")
            licenseHeaderFile(file("${project.rootDir}/spotless/LicenseHeader"))
        }
    }
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }

    // Android
    extra["androidConfig"] = { ex: Any ->
        (ex as? com.android.build.gradle.LibraryExtension)?.apply {

            compileSdk = libs.versions.compileSdk.get().toInt()

            defaultConfig {
                minSdk = libs.versions.minSdk.get().toInt()
                targetSdk = libs.versions.targetSdk.get().toInt()

                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                vectorDrawables {
                    useSupportLibrary = true
                }
            }

            buildTypes {
                debug {
                    isMinifyEnabled = false
                }
                create("qa") {
                    initWith(getByName("debug"))
                    isMinifyEnabled = true
                    proguardFiles(
                        getDefaultProguardFile("proguard-android-optimize.txt"),
                        "$rootDir/proguard-rules.pro"
                    )
                }
                release {
                    initWith(getByName("qa"))
                    (this as com.android.build.gradle.internal.dsl.BuildType).isDebuggable = false
                }
            }

            packagingOptions {
                resources {
                    excludes.add("META-INF/licenses/**")
                    excludes.add("META-INF/AL2.0")
                    excludes.add("META-INF/LGPL2.1")
                }
            }
        }
    }

    // Android for compose
    extra["composeAndroidConfig"] = { ex: Any ->
        (ex as? com.android.build.gradle.LibraryExtension)?.apply {
            val androidConfig: Any.() -> Unit by project.extra

            androidConfig()

            composeOptions {
                kotlinCompilerExtensionVersion = libs.versions.composeVersion.get()
            }

            buildFeatures {
                compose = true
            }
        }
    }

    // Android for ksp
    extra["kspAndroidConfig"] = { ex: Any, moduleName: String ->
        (ex as? com.android.build.gradle.LibraryExtension)?.apply {
            ksp {
                arg("compose-destinations.mode", "destinations")
                arg("compose-destinations.moduleName", moduleName)
                arg(
                    "compose-destinations.codeGenPackageName",
                    "ru.surfstudio.android.demo.$moduleName"
                )
            }
        }
    }

    // https://github.com/google/ksp/releases
    // sourceSets settings for ksp
    // run ./gradlew kspDebugKotlin to generate destinations
    extra["kspKotlinConfig"] = { ex: Any ->
        (ex as? org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension)?.apply {
            sourceSets {
                getByName("debug") {
                    kotlin.srcDir("build/generated/ksp/debug/kotlin")
                }
                getByName("qa") {
                    kotlin.srcDir("build/generated/ksp/qa/kotlin")
                }
                getByName("release") {
                    kotlin.srcDir("build/generated/ksp/release/kotlin")
                }
            }
        }
    }

    // connect internal libraries (local or remote)
    extra["internalDependencies"] = { ex: Any ->
        (ex as? DependencyHandlerScope)?.apply {
            val implementation by configurations

            implementation(libs.bundles.surfOther)

            if (findProperty("localConnectionEnabled").toString().toBoolean()) {
                implementation(project(":mvi-core"))
                implementation(project(":mvi-mappers"))
            } else {
                implementation(libs.bundles.surfMvi)
            }
        }
    }
}

fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.toUpperCase().contains(it) }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}