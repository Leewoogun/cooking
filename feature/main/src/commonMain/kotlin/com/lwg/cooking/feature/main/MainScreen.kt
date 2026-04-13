package com.lwg.cooking.feature.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
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
import com.lwg.cooking.designsystem.theme.CookingTheme
import com.lwg.cooking.feature.ex1.Ex1Route
import com.lwg.cooking.feature.ex2.Ex2Route
import com.lwg.cooking.feature.ex3.Ex3Route
import com.lwg.cooking.feature.home.HomeRoute
import com.lwg.cooking.feature.main.component.BottomNavItem
import com.lwg.cooking.navigation.Route

@Composable
fun MainRoute() {
    MainScreen()
}

@Composable
private fun MainScreen() {
    CookingTheme {
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
                        is Route.HomeRoute -> NavEntry(route) { HomeRoute() }
                        is Route.Ex1Route -> NavEntry(route) { Ex1Route() }
                        is Route.Ex2Route -> NavEntry(route) { Ex2Route() }
                        is Route.Ex3Route -> NavEntry(route) { Ex3Route() }
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
        BottomNavItem.entries.forEach { destination ->
            NavigationBarItem(
                selected = when (currentRoute) {
                    is Route.HomeRoute -> destination == BottomNavItem.HOME
                    is Route.Ex1Route -> destination == BottomNavItem.EX1
                    is Route.Ex2Route -> destination == BottomNavItem.EX2
                    is Route.Ex3Route -> destination == BottomNavItem.EX3
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
