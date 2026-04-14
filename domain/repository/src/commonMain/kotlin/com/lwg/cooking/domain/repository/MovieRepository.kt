package com.lwg.cooking.domain.repository

import com.lwg.cooking.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getTopRatedMovies(
        page: Int = 1,
        onError: (String) -> Unit,
    ): Flow<List<Movie>>
}
