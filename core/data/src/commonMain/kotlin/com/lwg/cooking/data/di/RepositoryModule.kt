package com.lwg.cooking.data.di

import com.lwg.cooking.data.repository.RecipeRepositoryImpl
import com.lwg.cooking.domain.repository.RecipeRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Repository 계층을 제공하는 Koin 모듈
 *
 * 구조:
 * - Domain 모듈: Repository 인터페이스 정의
 * - Data 모듈: Repository 구현체 (Network API 사용)
 */
val repositoryModule = module {
    // RecipeRepository
    singleOf(::RecipeRepositoryImpl) { bind<RecipeRepository>() }

    // 추가 Repository 예시:
    // singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    // singleOf(::AuthRepositoryImpl) { bind<AuthRepository>() }
}
