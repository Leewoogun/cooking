package com.lwg.cooking.network.di

import org.koin.core.module.Module

/**
 * Network 레이어의 모든 Koin 모듈을 통합
 *
 * 포함:
 * - ktorfitModule: Ktorfit 기본 설정 (HttpClient, Json, Ktorfit)
 * - apiModule: API 인터페이스들 (RecipeApi 등)
 *
 * 사용 방법:
 * ```kotlin
 * startKoin {
 *     modules(networkModules)
 * }
 * ```
 */
val networkModules: List<Module> = listOf(
    ktorfitModule,  // Ktorfit 기본 설정
    apiModule,      // API 인터페이스들
)
