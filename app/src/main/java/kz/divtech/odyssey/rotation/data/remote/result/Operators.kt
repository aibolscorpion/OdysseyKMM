@file:Suppress("unused")

package kz.divtech.odyssey.rotation.data.remote.result

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

fun <T> Result<T>.isSuccess(): Boolean {
    return this is Result.Success
}

fun <T> Result<T>.asSuccess(): Result.Success.HttpResponse<T> {
    return this as Result.Success.HttpResponse<T>
}

@OptIn(ExperimentalContracts::class)
fun <T> Result<T>.isFailure(): Boolean {
    contract {
        returns(true) implies (this@isFailure is Result.Failure<*>)
    }
    return this is Result.Failure<*>
}

@OptIn(ExperimentalContracts::class)
fun <T> Result<T>.isHttpException(): Boolean {
    contract {
        returns(true) implies (this@isHttpException is Result.Failure.HttpError)
    }
    return this is Result.Failure.HttpError
}

fun <T> Result<T>.asFailure(): Result.Failure<*> {
    return this as Result.Failure<*>
}


fun <T, R> Result<T>.map(transform: (value: T) -> R): Result<R> {
    return when(this) {
        is Result.Success -> Result.Success.Value(transform(value))
        is Result.Failure<*> -> this
    }
}

fun <T, R> Result<T>.flatMap(transform: (result: Result<T>) -> Result<R>): Result<R> {
    return transform(this)
}