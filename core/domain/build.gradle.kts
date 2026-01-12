plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.cookingKotlinMultiplatform)
}

android {
    namespace = "com.lwg.cooking.domain"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Coroutines (suspend 함수 사용)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
