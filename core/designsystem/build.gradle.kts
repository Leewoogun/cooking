plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.baseKotlinMultiplatform)
    alias(libs.plugins.baseComposeMultiplatform)
}

android {
    namespace = "com.lwg.designsystem"
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
        }
    }
}