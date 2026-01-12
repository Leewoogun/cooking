package com.lwg.cooking.network.di

import com.lwg.cooking.network.api.RecipeApi
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module

/**
 * API 인터페이스 제공 모듈
 *
 * 사용 방법:
 * 1. api/ 디렉토리에 Ktorfit 인터페이스를 작성합니다
 * 2. 프로젝트를 빌드하면 KSP가 자동으로 createXxxApi() extension function을 생성합니다
 * 3. 이 모듈에 single { get<Ktorfit>().createXxxApi() } 를 추가합니다
 *
 * 예시:
 * - RecipeApi 인터페이스 → createRecipeApi() 생성됨
 * - UserApi 인터페이스 → createUserApi() 생성됨
 */
val apiModule = module {
    // KSP가 생성한 extension function을 사용합니다
    // deprecated된 create<T>() 대신 createXxxApi() 사용

    // TODO: 프로젝트를 빌드한 후 아래 주석을 해제하세요
    // KSP가 Ktorfit.createRecipeApi() extension function을 생성합니다
    // single<RecipeApi> { get<Ktorfit>().createRecipeApi() }

    // 추가 API 예시:
    // single<UserApi> { get<Ktorfit>().createUserApi() }
    // single<AuthApi> { get<Ktorfit>().createAuthApi() }
}
