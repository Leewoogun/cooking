plugins {
    alias(libs.plugins.baseKotlinMultiplatformPure)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.domain.model)
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}
