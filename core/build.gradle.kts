plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

val composeAndroidConfig: Any.() -> Unit by project.extra
val internalDependencies: Any.() -> Unit by project.extra

android {
    this.composeAndroidConfig()
}

dependencies {
    implementation(project(":resources"))

    implementation(libs.bundles.compose)
    implementation(libs.bundles.core)
    implementation(libs.bundles.hilt)
    implementation(libs.bundles.network)

    internalDependencies()

    implementation(platform(libs.bom.firebase))
    implementation(libs.bundles.firebase)

    kapt(libs.bundles.hiltKapt)
}