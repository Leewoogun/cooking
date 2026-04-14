package com.lwg.base.data.di

import com.lwg.base.remote.api.di.ApiModule
import com.lwg.base.remote.network.di.NetworkModule
import org.koin.core.annotation.Module

/**
 * Data 레이어의 모든 Koin 모듈을 통합
 *
 * 포함:
 * - NetworkModule: Network 계층 (Ktorfit, HttpClient)
 * - ApiModule: API 인터페이스 계층
 * - RepositoryModule: Repository 계층
 */
@Module(includes = [NetworkModule::class, ApiModule::class, RepositoryModule::class])
class DataModule
