package com.lwg.cooking.network.di

import org.koin.core.annotation.Module

/**
 * API 인터페이스 제공 모듈
 *
 * 사용 방법:
 * 1. api/ 디렉토리에 Ktorfit 인터페이스를 작성합니다
 * 2. 프로젝트를 빌드하면 KSP가 자동으로 createXxxApi() extension function을 생성합니다
 * 3. 이 모듈에 @Single 어노테이션이 붙은 provide 함수를 추가합니다
 *
 * 예시:
 * @Single
 * fun provideRecipeApi(ktorfit: Ktorfit): RecipeApi = ktorfit.createRecipeApi()
 */
@Module
class ApiModule
