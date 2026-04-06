package com.lwg.cooking.di

import com.lwg.cooking.data.di.DataModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DataModule::class])
@ComponentScan("com.lwg.cooking")
class AppModule
