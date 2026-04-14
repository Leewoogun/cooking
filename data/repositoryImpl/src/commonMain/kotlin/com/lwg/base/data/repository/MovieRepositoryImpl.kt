package com.lwg.base.data.repository

import com.lwg.base.domain.model.Movie
import com.lwg.base.domain.repository.MovieRepository
import com.lwg.base.remote.api.MovieApi
import com.lwg.base.remote.mapper.toMovie
import com.lwg.base.remote.network.util.suspendOnFailureWithErrorHandling
import com.lwg.base.remote.network.util.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single(binds = [MovieRepository::class])
class MovieRepositoryImpl(
    private val movieApi: MovieApi,
) : MovieRepository {

    override fun getTopRatedMovies(page: Int, onError: (String) -> Unit): Flow<List<Movie>> = flow {
        movieApi.getTopRatedMovies(page)
            .suspendOnFailureWithErrorHandling(onError)
            .suspendOnSuccess {
                emit(response.results.map { it.toMovie() })
            }
    }
}
