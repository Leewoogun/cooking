package com.lwg.cooking.domain.repository

interface RecipeRepository {
    suspend fun getRecipes(onError: (String) -> Unit): List<String>

    suspend fun getRecipeById(id: String, onError: (String) -> Unit): String?
}
