package com.lwg.base

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
            cookingAppDeclaration {
                androidContext(this@BaseApplication)
            },
        )
    }
}