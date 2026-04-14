package com.lwg.base.convention

import com.lwg.base.libs
import com.lwg.base.primitive.DetektPlugin
import com.lwg.base.primitive.KotlinMultiPlatformAndroidPlugin
import com.lwg.base.primitive.KotlinMultiPlatformPlugin
import com.lwg.base.primitive.KotlinMultiPlatformiOSPlugin
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