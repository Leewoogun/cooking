plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.cookingKotlinMultiplatform)
    alias(libs.plugins.cookingComposeMultiplatform)
}

android {
    namespace = "com.lwg.utils"

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    sourceSets {
        androidMain.dependencies {

        }
        commonMain.dependencies {
            implementation(libs.kermit)
        }
        iosMain.dependencies {

        }
    }
}
