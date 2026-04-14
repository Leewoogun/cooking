plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.baseKotlinMultiplatform)
    alias(libs.plugins.baseComposeMultiplatform)
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
