package com.lwg.cooking.data.repository

import com.lwg.cooking.domain.repository.MovieRepository
import com.lwg.cooking.network.api.MovieApi
import com.lwg.cooking.network.util.suspendOnFailureWithErrorHandling
import com.lwg.cooking.network.util.suspendOnSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.annotation.Single

@Single(binds = [MovieRepository::class])
class MovieRepositoryImpl(
    private val movieApi: MovieApi,
) : MovieRepository {

    override fun getTopRatedMovies(page: Int, onError: (String) -> Unit): Flow<List<String>> = flow {
        movieApi.getTopRatedMovies(page)
            .suspendOnFailureWithErrorHandling(onError)
            .suspendOnSuccess {
                emit(response.results.map { it.title })
            }
    }
}
