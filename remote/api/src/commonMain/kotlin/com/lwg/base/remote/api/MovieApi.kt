package com.lwg.base.remote.api

import com.lwg.base.remote.model.TopRatedMovieResponse
import com.lwg.base.remote.network.util.ApiResult
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface MovieApi {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
    ): ApiResult<TopRatedMovieResponse>
}
