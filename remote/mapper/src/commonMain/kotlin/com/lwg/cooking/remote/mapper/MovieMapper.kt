package com.lwg.cooking.remote.mapper

import com.lwg.cooking.domain.model.Movie
import com.lwg.cooking.remote.model.MovieData

fun MovieData.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        originalTitle = original_title,
        overview = overview,
        popularity = popularity,
        posterUrl = poster_path.takeIf { it.isNotEmpty() }
            ?.let { "https://image.tmdb.org/t/p/w500$it" } ?: "",
        releaseDate = release_date,
        voteAverage = vote_average,
        voteCount = vote_count,
    )
}
