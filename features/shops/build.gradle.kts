// FIXME: Remove once https://youtrack.jetbrains.com/issue/KTIJ-19369 is fixed
@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.ksp.plugin)
}

val composeAndroidConfig: Any.() -> Unit by project.extra
val kspAndroidConfig: Any.(String) -> Unit by project.extra
val kspKotlinConfig: Any.() -> Unit by project.extra
val internalDependencies: Any.() -> Unit by project.extra

android {
    this.composeAndroidConfig()
    this.kspAndroidConfig("shops")
}

kotlin {
    this.kspKotlinConfig()
}

dependencies {
    implementation(project(":components"))
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":network"))

    implementation(libs.bundles.compose)
    implementation(libs.bundles.hilt)

    internalDependencies()

    implementation(libs.compose.destinations.core)
    ksp(libs.compose.destinations.ksp)

    kapt(libs.bundles.hiltKapt)
}