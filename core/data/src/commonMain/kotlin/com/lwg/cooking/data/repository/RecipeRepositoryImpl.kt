package com.lwg.cooking.data.repository

import com.lwg.cooking.domain.repository.RecipeRepository
import com.lwg.cooking.network.api.RecipeApi

/**
 * RecipeRepository의 실제 구현체
 *
 * Data 계층에 위치:
 * - Domain 계층의 RecipeRepository 인터페이스를 구현
 * - Network 모듈의 RecipeApi를 사용하여 실제 데이터 로드
 * - 에러 처리 및 Result 래핑
 *
 * @param recipeApi Network 모듈에서 제공하는 RecipeApi 주입
 */
class RecipeRepositoryImpl(
    private val recipeApi: RecipeApi
) : RecipeRepository {

    override suspend fun getRecipes(): Result<List<String>> {
        return try {
            val recipes = recipeApi.getRecipes()
            Result.success(recipes)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRecipeById(id: String): Result<String> {
        return try {
            val recipe = recipeApi.getRecipeById(id)
            Result.success(recipe)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

