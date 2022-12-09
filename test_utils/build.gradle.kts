plugins {
    id("com.android.library")
    kotlin("android")
}

val androidConfig: Any.() -> Unit by project.extra
val internalDependencies: Any.() -> Unit by project.extra

android {
    this.androidConfig()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":domain"))
    implementation(project(":network"))

    api(libs.bundles.test)
    implementation(libs.bundles.core)
    implementation(libs.bundles.network)
    implementation(libs.bundles.room)

    internalDependencies()
}