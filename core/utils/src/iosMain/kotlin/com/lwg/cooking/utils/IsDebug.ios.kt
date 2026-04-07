package com.lwg.cooking.utils

import kotlin.experimental.ExperimentalNativeApi

@OptIn(ExperimentalNativeApi::class)
actual val isDebug: Boolean = kotlin.native.Platform.isDebugBinary
