package com.lwg.cooking.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lwg.cooking.domain.repository.MovieRepository
import com.lwg.cooking.utils.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class MovieViewModel(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private val _movieTitles = MutableStateFlow<List<String>>(emptyList())
    val movieTitles: StateFlow<List<String>> = _movieTitles

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loadMovies()
    }

    private fun loadMovies() {
        Logger.i("로그입니다")
        viewModelScope.launch {
            movieRepository.getTopRatedMovies { errorMessage ->
                _error.value = errorMessage
            }.collect { _movieTitles.value = it }
        }
    }
}
