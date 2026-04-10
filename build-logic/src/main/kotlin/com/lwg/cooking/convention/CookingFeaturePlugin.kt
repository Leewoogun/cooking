package com.lwg.cooking.convention

import com.lwg.cooking.library
import com.lwg.cooking.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class CookingFeaturePlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("androidLibrary").get().get().pluginId)
        }

        apply(plugin = "cooking.kotlin.multiplatform")
        apply(plugin = "cooking.compose.multiplatform")
        apply(plugin = "cooking.ksp.koin")

        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.apply {
                commonMain {
                    dependencies {
                        implementation(project(":core:designsystem"))
                        implementation(project(":core:navigation"))
                        implementation(project(":core:domain"))
                        implementation(project(":core:utils"))
                        implementation(libs.library("androidx-navigation3-ui"))
                        implementation(libs.library("androidx-lifecycle-viewmodel-navigation3"))
                        implementation(libs.library("androidx-lifecycle-runtime-compose"))
                        implementation(libs.library("koin-compose-viewmodel-navigation"))
                        implementation(libs.library("koin-annotations"))
                    }
                }
            }
        }
    }
}
