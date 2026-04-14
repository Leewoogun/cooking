plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.cookingKotlinMultiplatform)
    alias(libs.plugins.cookingComposeMultiplatform)
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