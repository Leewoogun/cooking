package com.lwg.cooking.data.di

import com.lwg.cooking.network.di.networkModules
import org.koin.core.module.Module

/**
 * Data 레이어의 모든 Koin 모듈을 통합
 *
 * 포함:
 * - networkModules: Network 계층 (Ktorfit, API 인터페이스)
 * - repositoryModule: Repository 계층 (비즈니스 로직)
 *
 * 사용 방법:
 * ```kotlin
 * startKoin {
 *     modules(dataModules)
 * }
 * ```
 */
val dataModules: List<Module> = networkModules + listOf(
    repositoryModule,
)
