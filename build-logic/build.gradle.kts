import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.roborazzi.gradle.plugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    implementation(libs.room.runtime)
}

gradlePlugin {
    plugins {
        register("kmpIos") {
            id = "base.kmp.ios"
            implementationClass = "com.lwg.base.primitive.KotlinMultiPlatformiOSPlugin"
        }
        register("kmpAndroid") {
            id = "base.kmp.android"
            implementationClass = "com.lwg.base.primitive.KotlinMultiPlatformAndroidPlugin"
        }
        register("kmpPrimitive") {
            id = "base.kmp"
            implementationClass = "com.lwg.base.primitive.KotlinMultiPlatformPlugin"
        }
        register("detekt") {
            id = "base.verify.detekt"
            implementationClass = "com.lwg.base.primitive.DetektPlugin"
        }
        register("kspKoin") {
            id = "base.ksp.koin"
            implementationClass = "com.lwg.base.primitive.KspKoinPlugin"
        }
        register("kmpConvention") {
            id = "base.kotlin.multiplatform"
            implementationClass = "com.lwg.base.convention.KotlinMultiPlatformConventionPlugin"
        }
        register("cmpConvention") {
            id = "base.compose.multiplatform"
            implementationClass = "com.lwg.base.convention.ComposeMultiPlatformConventionPlugin"
        }
        register("kmpPureConvention") {
            id = "base.kotlin.multiplatform.pure"
            implementationClass = "com.lwg.base.convention.KotlinMultiPlatformPurePlugin"
        }
        register("baseFeature") {
            id = "base.feature"
            implementationClass = "com.lwg.base.convention.BaseFeaturePlugin"
        }
    }
}