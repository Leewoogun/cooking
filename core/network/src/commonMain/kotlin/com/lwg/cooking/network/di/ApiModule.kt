package com.lwg.cooking.network.di

import com.lwg.cooking.network.api.MovieApi
import com.lwg.cooking.network.api.createMovieApi
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class ApiModule {

    @Single
    fun provideMovieApi(ktorfit: Ktorfit): MovieApi = ktorfit.createMovieApi()
}
