package com.lwg.cooking.network.api

import com.lwg.cooking.network.model.TopRatedMovieResponse
import com.lwg.cooking.network.util.ApiResult
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface MovieApi {

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page: Int = 1,
    ): ApiResult<TopRatedMovieResponse>
}
