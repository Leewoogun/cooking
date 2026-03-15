package com.lwg.cooking.network.di

import org.koin.core.annotation.Module

/**
 * Network 레이어의 모든 Koin 모듈을 통합
 *
 * KtorfitModule, ApiModule을 includes로 포함합니다.
 */
@Module(includes = [KtorfitModule::class, ApiModule::class])
class NetworkModule
