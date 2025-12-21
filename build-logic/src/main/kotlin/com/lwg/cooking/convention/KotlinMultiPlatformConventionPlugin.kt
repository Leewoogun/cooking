package com.lwg.cooking.convention

import com.lwg.cooking.libs
import com.lwg.cooking.primitive.DetektPlugin
import com.lwg.cooking.primitive.KotlinMultiPlatformAndroidPlugin
import com.lwg.cooking.primitive.KotlinMultiPlatformPlugin
import com.lwg.cooking.primitive.KotlinMultiPlatformiOSPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

class KotlinMultiPlatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(libs.findPlugin("kotlinMultiplatform").get().get().pluginId)
        }

        apply<KotlinMultiPlatformPlugin>()
        apply<KotlinMultiPlatformAndroidPlugin>()
        apply<KotlinMultiPlatformiOSPlugin>()
        apply<DetektPlugin>()
    }
}