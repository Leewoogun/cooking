package com.lwg.cooking

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.lwg.cooking.di.AppModule
import com.lwg.cooking.feature.ex1.Ex1Screen
import com.lwg.cooking.feature.ex2.Ex2Screen
import com.lwg.cooking.feature.ex3.Ex3Screen
import com.lwg.cooking.feature.home.HomeScreen
import com.lwg.cooking.navigation.Route
import com.lwg.cooking.navigation.TopLevelDestination
import org.koin.core.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import org.koin.ksp.generated.module

@Composable
fun App() {
    MaterialTheme {
        val backStack = remember { mutableStateListOf<Route>(Route.HomeRoute.Main) }
        val currentRoute = backStack.lastOrNull()

        Scaffold(
            bottomBar = {
                CookingBottomBar(
                    currentRoute = currentRoute,
                    onTabSelected = { route ->
                        backStack.clear()
                        backStack.add(route)
                    },
                )
            },
        ) { innerPadding ->
            NavDisplay(
                backStack = backStack,
                onBack = { backStack.removeAt(backStack.lastIndex) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                entryProvider = { route ->
                    when (route) {
                        is Route.HomeRoute -> NavEntry(route) { HomeScreen() }
                        is Route.Ex1Route -> NavEntry(route) { Ex1Screen() }
                        is Route.Ex2Route -> NavEntry(route) { Ex2Screen() }
                        is Route.Ex3Route -> NavEntry(route) { Ex3Screen() }
                    }
                },
            )
        }
    }
}

@Composable
private fun CookingBottomBar(
    currentRoute: Route?,
    onTabSelected: (Route) -> Unit,
) {
    NavigationBar {
        TopLevelDestination.entries.forEach { destination ->
            NavigationBarItem(
                selected = when (currentRoute) {
                    is Route.HomeRoute -> destination == TopLevelDestination.HOME
                    is Route.Ex1Route -> destination == TopLevelDestination.EX1
                    is Route.Ex2Route -> destination == TopLevelDestination.EX2
                    is Route.Ex3Route -> destination == TopLevelDestination.EX3
                    else -> false
                },
                onClick = { onTabSelected(destination.route) },
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.label,
                    )
                },
                label = { Text(text = destination.label) },
            )
        }
    }
}

internal fun cookingAppDeclaration(
    additionalDeclaration: KoinApplication.() -> Unit = {},
): KoinAppDeclaration = {
    modules(AppModule().module)
    additionalDeclaration()
}
