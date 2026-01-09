package com.lwg.cooking.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun CookingTheme(
    content: @Composable () -> Unit,
) {
    val typography = createTypography()

    CompositionLocalProvider(
        LocalTypography provides typography,
        LocalColorScheme provides CookingColorScheme.lightColorScheme
    ) {
        MaterialTheme(
            content = content,
        )
    }
}

object CookingTheme {

    val colorScheme: CookingColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography: CookingTypoGraphy
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}
