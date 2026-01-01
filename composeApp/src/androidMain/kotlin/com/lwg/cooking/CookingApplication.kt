package com.lwg.cooking

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CookingApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
            cookingAppDeclaration {
                androidContext(this@CookingApplication)
            },
        )
    }
}