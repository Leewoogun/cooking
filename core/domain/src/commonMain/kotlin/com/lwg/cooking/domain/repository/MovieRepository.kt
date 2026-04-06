package com.lwg.cooking.domain.repository

import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getTopRatedMovies(
        page: Int = 1,
        onError: (String) -> Unit,
    ): Flow<List<String>>
}
