import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

val internalDependencies: Any.() -> Unit by project.extra

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.surf.template"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        create("release") {
            val storePassword = System.getenv("storePassword")
            val keyPassword = System.getenv("storePassword")
            val keyAlias = System.getenv("keyAlias")
            var storeFile = System.getenv("storeFile")

            val releaseKeystorePropsFile =
                rootProject.file("keystore/doctor_keystore_release.properties")
            if (releaseKeystorePropsFile.exists()) {
                println("Start extract release keystore config from doctor_keystore_release.properties")
                val keystoreProps = Properties()
                keystoreProps.load(FileInputStream(releaseKeystorePropsFile))
            } else {
                println("Start extract release keystore config from global vars")
            }
            if (storeFile.isNullOrBlank()) {
                storeFile = "no_keystore_file" //fix crash file(storeFile)
            }
            println("Extracted keystore config: $storePassword, $keyPassword, $keyAlias, $storeFile")

            this.keyAlias = keyAlias
            this.keyPassword = keyPassword
            this.storeFile = file(storeFile)
            this.storePassword = storePassword
        }
        create("qa") {
            storeFile = file("../keystore/test.keystore")
            storePassword = "qatest"
            keyAlias = "test"
            keyPassword = "qatest"
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
        create("qa") {
            isMinifyEnabled = true
            isDebuggable = true
            signingConfig = signingConfigs["qa"]
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "$rootDir/proguard-rules.pro"
            )
            applicationIdSuffix = ".qa"
        }
        release {
            isMinifyEnabled = true
            isDebuggable = false
            signingConfig = signingConfigs["qa"] // todo change to release for real app
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "$rootDir/proguard-rules.pro"
            )
        }
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeVersion.get()
    }

    buildFeatures {
        compose = true
    }

    packagingOptions {
        resources {
            excludes.add("META-INF/licenses/**")
            excludes.add("META-INF/AL2.0")
            excludes.add("META-INF/LGPL2.1")
        }
    }
}

dependencies {
    implementation(project(":components"))
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":network"))
    testImplementation(project(":test_utils"))

    val qaImplementation by configurations

    debugImplementation(project(":debug"))
    qaImplementation(project(":debug"))
    releaseImplementation(project(":debug_no_op"))
    implementation(project(":features:basket"))

    implementation(project(":features:catalog"))
    implementation(project(":features:feed"))
    implementation(project(":features:profile"))
    implementation(project(":features:shops"))

    implementation(libs.bundles.compose)
    implementation(libs.bundles.core)
    implementation(libs.bundles.hilt)
    testImplementation(libs.bundles.network)

    internalDependencies()

    implementation(libs.compose.destinations.core)

    implementation(platform(libs.bom.firebase))
    implementation(libs.bundles.firebase)

    kapt(libs.bundles.hiltKapt)

    debugImplementation(libs.bundles.composeDebug)
    androidTestImplementation(libs.bundles.androidTest)
}