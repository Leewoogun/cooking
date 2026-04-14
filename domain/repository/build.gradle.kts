plugins {
    alias(libs.plugins.cookingKotlinMultiplatformPure)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.model)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
