package com.lwg.base

import androidx.compose.ui.window.ComposeUIViewController
import com.lwg.base.feature.main.MainRoute
import org.koin.compose.KoinApplication

fun MainViewController() = ComposeUIViewController {
    KoinApplication(
        application = cookingAppDeclaration(),
    ) {
        MainRoute()
    }
}
