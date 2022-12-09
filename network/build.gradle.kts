plugins {
    id("com.android.library")
    id("kotlinx-serialization")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

val androidConfig: Any.() -> Unit by project.extra
val internalDependencies: Any.() -> Unit by project.extra

android {
    this.androidConfig()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":core"))

    val qaImplementation by configurations

    debugImplementation(project(":debug"))
    qaImplementation(project(":debug"))
    releaseImplementation(project(":debug_no_op"))

    implementation(libs.bundles.hilt)
    implementation(libs.bundles.network)
    implementation(libs.bundles.room)
    implementation(libs.bundles.surfOther)

    internalDependencies()

    debugImplementation(libs.bundles.networkDebug)
    qaImplementation(libs.bundles.networkDebug)
    releaseImplementation(libs.bundles.releaseNoOp)

    kapt(libs.bundles.hiltKapt)
    kapt(libs.bundles.roomKapt)
}