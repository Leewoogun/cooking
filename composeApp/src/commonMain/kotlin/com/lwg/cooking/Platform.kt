package com.lwg.cooking

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform