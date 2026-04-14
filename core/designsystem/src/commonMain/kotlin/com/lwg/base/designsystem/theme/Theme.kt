package com.lwg.base.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable

@Composable
fun AppTheme(
    content: @Composable () -> Unit,
) {
    val typography = createTypography()

    CompositionLocalProvider(
        LocalTypography provides typography,
        LocalColorScheme provides AppColorScheme.lightColorScheme
    ) {
        MaterialTheme(
            content = content,
        )
    }
}

object AppTheme {

    val colorScheme: AppColorScheme
        @Composable
        @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography: AppTypoGraphy
        @Composable
        @ReadOnlyComposable
        get() = LocalTypography.current
}
