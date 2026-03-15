plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.cookingKotlinMultiplatform)
    alias(libs.plugins.ksp)
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
            implementation(libs.koin.annotations)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)
        }
    }
}

// KSP 생성 코드를 commonMain에 포함
kotlin.sourceSets.commonMain {
    kotlin.srcDir("build/generated/ksp/metadata/commonMain/kotlin")
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
    add("kspAndroid", libs.koin.ksp.compiler)
    add("kspIosX64", libs.koin.ksp.compiler)
    add("kspIosArm64", libs.koin.ksp.compiler)
    add("kspIosSimulatorArm64", libs.koin.ksp.compiler)
}

// KMP + KSP 태스크 의존성 workaround
project.tasks.configureEach {
    if (name != "kspCommonMainKotlinMetadata" &&
        (name.startsWith("ksp") || name.startsWith("compile"))
    ) {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
