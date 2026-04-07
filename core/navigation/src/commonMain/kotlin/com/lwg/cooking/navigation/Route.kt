package com.lwg.cooking.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    sealed interface HomeRoute : Route {

        @Serializable
        data object Main : HomeRoute
    }

    @Serializable
    sealed interface Ex1Route : Route {

        @Serializable
        data object Main : Ex1Route
    }

    @Serializable
    sealed interface Ex2Route : Route {

        @Serializable
        data object Main : Ex2Route
    }

    @Serializable
    sealed interface Ex3Route : Route {

        @Serializable
        data object Main : Ex3Route
    }
}
