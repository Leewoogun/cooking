package com.lwg.cooking.data.repository

import com.lwg.cooking.domain.repository.RecipeRepository
import com.lwg.cooking.network.api.RecipeApi
import com.lwg.cooking.network.util.onFailureWithErrorHandling
import com.lwg.cooking.network.util.onSuccess
import org.koin.core.annotation.Single

@Single(binds = [RecipeRepository::class])
class RecipeRepositoryImpl(
    private val recipeApi: RecipeApi,
) : RecipeRepository {

    override suspend fun getRecipes(onError: (String) -> Unit): List<String> {
        var result = emptyList<String>()

        recipeApi.getRecipes()
            .onFailureWithErrorHandling(onError)
            .onSuccess { result = response }

        return result
    }

    override suspend fun getRecipeById(id: String, onError: (String) -> Unit): String? {
        var result: String? = null

        recipeApi.getRecipeById(id)
            .onFailureWithErrorHandling(onError)
            .onSuccess { result = response }

        return result
    }
}
