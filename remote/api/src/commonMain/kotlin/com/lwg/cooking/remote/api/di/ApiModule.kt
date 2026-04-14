package com.lwg.cooking.remote.api.di

import com.lwg.cooking.remote.api.MovieApi
import com.lwg.cooking.remote.api.createMovieApi
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class ApiModule {

    @Single
    fun provideMovieApi(ktorfit: Ktorfit): MovieApi = ktorfit.createMovieApi()
}
