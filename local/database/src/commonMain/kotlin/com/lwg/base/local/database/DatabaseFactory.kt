package com.lwg.base.local.database

import androidx.room.RoomDatabase

internal expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
