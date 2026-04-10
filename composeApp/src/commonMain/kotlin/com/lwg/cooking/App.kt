package com.lwg.cooking

import com.lwg.cooking.di.AppModule
import com.lwg.cooking.feature.home.di.HomeModule
import org.koin.core.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

internal fun cookingAppDeclaration(
    additionalDeclaration: KoinApplication.() -> Unit = {},
): KoinAppDeclaration = {
    modules(
        AppModule().module,
        HomeModule().module,
    )
    additionalDeclaration()
}
