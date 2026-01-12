plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.cookingKotlinMultiplatform)
}

android {
    namespace = "com.lwg.cooking.data"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Domain 모듈 (Repository 인터페이스)
            implementation(projects.core.domain)

            // Network 모듈 (Ktorfit API 제공)
            implementation(projects.core.network)

            // Koin
            implementation(libs.koin.core)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
