package com.lwg.base.di

import com.lwg.base.data.di.DataModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DataModule::class])
@ComponentScan("com.lwg.base")
class AppModule
