package com.lwg.cooking.domain.usecase

import com.lwg.cooking.domain.model.Movie
import com.lwg.cooking.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetTopRatedMoviesUseCase(
    private val movieRepository: MovieRepository,
) {

    operator fun invoke(
        page: Int = 1,
        onError: (String) -> Unit,
    ): Flow<List<Movie>> {
        return movieRepository.getTopRatedMovies(
            page = page,
            onError = onError,
        )
    }
}
