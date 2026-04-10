package com.lwg.cooking

import androidx.compose.ui.window.ComposeUIViewController
import com.lwg.cooking.feature.main.MainRoute
import org.koin.compose.KoinApplication

fun MainViewController() = ComposeUIViewController {
    KoinApplication(
        application = cookingAppDeclaration(),
    ) {
        MainRoute()
    }
}
