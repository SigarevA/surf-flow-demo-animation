plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

val composeAndroidConfig: Any.() -> Unit by project.extra

android {
    this.composeAndroidConfig()
}

dependencies {
    implementation(project(":components"))
    implementation(project(":domain"))

    implementation(libs.bundles.compose)
    implementation(libs.bundles.core)
    implementation(libs.bundles.debug)
    implementation(libs.bundles.hilt)

    kapt(libs.bundles.hiltKapt)
}