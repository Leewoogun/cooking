plugins {
    alias(libs.plugins.cookingKotlinMultiplatformPure)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.model)
            implementation(projects.domain.repository)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
