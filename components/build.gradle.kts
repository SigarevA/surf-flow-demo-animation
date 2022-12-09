plugins {
    id("com.android.library")
    kotlin("android")
}

val composeAndroidConfig: Any.() -> Unit by project.extra
val internalDependencies: Any.() -> Unit by project.extra

android {
    this.composeAndroidConfig()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":resources"))

    api(libs.bundles.accompanist)
    implementation(libs.bundles.compose)
    debugApi(libs.bundles.composeDebug)

    implementation(libs.compose.destinations.core)

    // todo fix compose module preview https://issuetracker.google.com/issues/227767363
    debugApi("androidx.customview:customview:1.2.0-alpha01")
    debugApi("androidx.customview:customview-poolingcontainer:1.0.0-beta02")

    internalDependencies()
}