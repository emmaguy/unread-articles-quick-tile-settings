package com.emmaguy.quicktilepocket.feature

import retrofit2.adapter.rxjava.Result
import timber.log.Timber

object RetrofitExtensions {
    fun <T> Result<T>.success(): Boolean? {
        Timber.d("RetrofitExtensions, message: '${response().message()}' error code: ${response().code()}")
        return !isError && response().isSuccessful && response().body() != null
    }
}
