package com.lwg.base.remote.api.di

import com.lwg.base.remote.api.MovieApi
import com.lwg.base.remote.api.createMovieApi
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class ApiModule {

    @Single
    fun provideMovieApi(ktorfit: Ktorfit): MovieApi = ktorfit.createMovieApi()
}
