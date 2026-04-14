plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.cookingKotlinMultiplatform)
    alias(libs.plugins.cookingKspKoin)
}

android {
    namespace = "com.lwg.cooking.data.repositoryimpl"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            // Domain
            implementation(projects.domain.model)
            implementation(projects.domain.repository)

            // Remote
            implementation(projects.remote.api)
            implementation(projects.remote.model)
            implementation(projects.remote.mapper)
            implementation(projects.remote.network)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
