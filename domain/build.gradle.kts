plugins {
    id("com.android.library")
    kotlin("android")
}

val composeAndroidConfig: Any.() -> Unit by project.extra

android {
    this.composeAndroidConfig()
}

dependencies {
    implementation(libs.bundles.compose)
}