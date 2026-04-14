package com.lwg.base.domain.usecase

import com.lwg.base.domain.model.Movie
import com.lwg.base.domain.repository.MovieRepository
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
