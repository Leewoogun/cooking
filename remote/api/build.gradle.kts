plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.baseKotlinMultiplatform)
    alias(libs.plugins.baseKspKoin)
    alias(libs.plugins.ktorfit)
}

android {
    namespace = "com.lwg.base.remote.api"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.remote.network)
            implementation(projects.remote.model)

            // Ktorfit
            implementation(libs.ktorfit.lib)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.ktorfit.ksp)
    add("kspAndroid", libs.ktorfit.ksp)
    add("kspIosArm64", libs.ktorfit.ksp)
    add("kspIosSimulatorArm64", libs.ktorfit.ksp)
}
