package com.lwg.cooking.network.api

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

/**
 * Ktorfit API 인터페이스 예제
 *
 * 사용 방법:
 * 1. KSP가 자동으로 구현 코드를 생성합니다
 * 2. Koin 모듈에서 createRecipeApi()로 인스턴스를 생성합니다
 */
interface RecipeApi {

    @GET("recipes")
    suspend fun getRecipes(): List<String> // TODO: 실제 Response 모델로 변경

    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: String): String // TODO: 실제 Response 모델로 변경
}
