package com.lwg.cooking.data.di

import com.lwg.cooking.network.di.NetworkModule
import org.koin.core.annotation.Module

/**
 * Data 레이어의 모든 Koin 모듈을 통합
 *
 * 포함:
 * - NetworkModule: Network 계층 (Ktorfit, API 인터페이스)
 * - RepositoryModule: Repository 계층 (비즈니스 로직)
 *
 * 사용 방법:
 * ```kotlin
 * startKoin {
 *     modules(DataModule().module)
 * }
 * ```
 */
@Module(includes = [NetworkModule::class, RepositoryModule::class])
class DataModule
