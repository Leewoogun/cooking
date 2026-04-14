package com.lwg.base.domain.repository

import com.lwg.base.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    fun getTopRatedMovies(
        page: Int = 1,
        onError: (String) -> Unit,
    ): Flow<List<Movie>>
}
