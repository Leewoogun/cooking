package com.lwg.cooking.network.api

import com.lwg.cooking.network.util.ApiResult
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface RecipeApi {

    @GET("recipes")
    suspend fun getRecipes(): ApiResult<List<String>> // TODO: 실제 Response 모델로 변경

    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") id: String): ApiResult<String> // TODO: 실제 Response 모델로 변경
}
