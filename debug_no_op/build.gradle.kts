plugins {
    id("com.android.library")
    kotlin("android")
}

val androidConfig: Any.() -> Unit by project.extra

android {
    this.androidConfig()
}

dependencies {
    implementation(project(":domain"))
}