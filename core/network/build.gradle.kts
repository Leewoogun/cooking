plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.cookingKotlinMultiplatform)
    alias(libs.plugins.cookingComposeMultiplatform)
}

android {
    namespace = "com.lwg.network"
}

kotlin {
    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {

        }
        iosMain.dependencies {

        }
    }
}
