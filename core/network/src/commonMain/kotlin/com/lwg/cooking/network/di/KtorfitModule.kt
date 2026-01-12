package com.lwg.cooking.network.di

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

/**
 * Ktorfit 기본 설정 모듈
 * - Json 직렬화 설정
 * - HttpClient 설정
 * - Ktorfit 인스턴스 제공
 */
val ktorfitModule = module {
    single { provideJson() }
    single { provideHttpClient(get()) }
    single { provideKtorfit(get()) }
}

private fun provideJson(): Json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    prettyPrint = true
}

private fun provideHttpClient(json: Json): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(json)
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("HTTP Client: $message")
                }
            }
            level = LogLevel.ALL
        }
    }
}

private fun provideKtorfit(httpClient: HttpClient): Ktorfit {
    return Ktorfit.Builder()
        .httpClient(httpClient)
        .baseUrl("https://api.example.com/") // TODO: 실제 API URL로 변경
        .build()
}
