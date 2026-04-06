package com.lwg.cooking.network.util

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed interface ApiResult<out T> {
    data class Success<T>(val response: T) : ApiResult<T>

    sealed interface Failure : ApiResult<Nothing> {
        data class HttpError(
            val statusCode: Int,
            val statusMessage: String,
        ) : Failure

        data object NetworkError : Failure

        data class UnknownError(val throwable: Throwable) : Failure
    }
}

@OptIn(ExperimentalContracts::class)
inline fun <T> ApiResult<T>.onSuccess(
    crossinline action: ApiResult.Success<T>.() -> Unit,
): ApiResult<T> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (this is ApiResult.Success) {
        action(this)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
suspend inline fun <T> ApiResult<T>.suspendOnSuccess(
    crossinline action: suspend ApiResult.Success<T>.() -> Unit,
): ApiResult<T> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (this is ApiResult.Success) {
        action(this)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T> ApiResult<T>.onFailure(
    crossinline action: ApiResult.Failure.() -> Unit,
): ApiResult<T> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (this is ApiResult.Failure) {
        action(this)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
suspend inline fun <T> ApiResult<T>.suspendOnFailure(
    crossinline action: suspend ApiResult.Failure.() -> Unit,
): ApiResult<T> {
    contract { callsInPlace(action, InvocationKind.AT_MOST_ONCE) }
    if (this is ApiResult.Failure) {
        action(this)
    }
    return this
}

@OptIn(ExperimentalContracts::class)
inline fun <T> ApiResult<T>.onFailureWithErrorHandling(
    crossinline onError: (String) -> Unit = {},
): ApiResult<T> {
    contract { callsInPlace(onError, InvocationKind.AT_MOST_ONCE) }
    if (this is ApiResult.Failure) {
        when (this) {
            is ApiResult.Failure.HttpError -> {
                onError(statusMessage)
            }

            ApiResult.Failure.NetworkError -> {
                onError("네트워크 오류가 발생하였습니다. 네트워크를 확인하여 주세요.")
            }

            is ApiResult.Failure.UnknownError -> {
                onError(throwable.message ?: "알 수 없는 오류가 발생하였습니다.")
            }
        }
    }
    return this
}
