package com.lwg.cooking.feature.home.contract

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface HomeUiState {

    @Immutable
    data object Loading : HomeUiState

    @Immutable
    data object Empty : HomeUiState

    @Immutable
    data class Data(
        val movieTitles: List<String>,
    ) : HomeUiState {
        val isEmpty: Boolean get() = movieTitles.isEmpty()
    }
}
