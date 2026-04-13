package com.lwg.cooking.navigation

import androidx.compose.runtime.compositionLocalOf

interface NavigateAction {
    fun navigateTo(route: Route)
    fun popBackStack()
    fun popTo(route: Route)
    fun switchTab(route: Route)
}

val LocalNavigateAction = compositionLocalOf<NavigateAction> {
    error("No NavigateAction provided")
}
