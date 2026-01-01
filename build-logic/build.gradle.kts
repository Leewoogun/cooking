import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.roborazzi.gradle.plugin)
    compileOnly(libs.compose.compiler.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kmpIos") {
            id = "cooking.kmp.ios"
            implementationClass = "com.lwg.cooking.primitive.KotlinMultiPlatformiOSPlugin"
        }
        register("kmpAndroid") {
            id = "cooking.kmp.android"
            implementationClass = "com.lwg.cooking.primitive.KotlinMultiPlatformAndroidPlugin"
        }
        register("kmpPrimitive") {
            id = "cooking.kmp"
            implementationClass = "com.lwg.cooking.primitive.KotlinMultiPlatformPlugin"
        }
        register("detekt") {
            id = "cooking.verify.detekt"
            implementationClass = "com.lwg.cooking.primitive.DetektPlugin"
        }
        register("kmpConvention") {
            id = "cooking.kotlin.multiplatform"
            implementationClass = "com.lwg.cooking.convention.KotlinMultiPlatformConventionPlugin"
        }
        register("cmpConvention") {
            id = "cooking.compose.multiplatform"
            implementationClass = "com.lwg.cooking.convention.ComposeMultiPlatformConventionPlugin"
        }
        register("cookingFeature") {
            id = "cooking.feature"
            implementationClass = "com.lwg.cooking.convention.CookingFeaturePlugin"
        }
    }
}