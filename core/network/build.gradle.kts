import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.cookingKotlinMultiplatform)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.cookingKspKoin)
    alias(libs.plugins.ktorfit)
    alias(libs.plugins.buildkonfig)
}

android {
    namespace = "com.lwg.cooking.network"
}

val properties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

buildkonfig {
    packageName = "com.lwg.cooking.network"

    defaultConfigs {
        buildConfigField(STRING, "TMDB_TOKEN", properties["tmdb_token"].toString().trim('"'))
        buildConfigField(STRING, "BASE_URL", "https://api.themoviedb.org/3/")
    }
}

kotlin {
    sourceSets {
        androidMain.dependencies {
            implementation(libs.ktor.client.okhttp)
        }
        commonMain.dependencies {
            // Ktorfit
            implementation(libs.ktorfit.lib)

            // Ktor Client
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.logging)

            // Kotlinx Serialization
            implementation(libs.kotlinx.serialization.json)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.annotations)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.ktorfit.ksp)
    add("kspAndroid", libs.ktorfit.ksp)
    add("kspIosX64", libs.ktorfit.ksp)
    add("kspIosArm64", libs.ktorfit.ksp)
    add("kspIosSimulatorArm64", libs.ktorfit.ksp)
}
