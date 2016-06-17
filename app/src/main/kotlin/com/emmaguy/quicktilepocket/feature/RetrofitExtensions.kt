package com.emmaguy.quicktilepocket.feature

import retrofit2.adapter.rxjava.Result

object RetrofitExtensions {
    fun <T> Result<T>.success(): Boolean? {
        return !isError && response().isSuccessful && response().body() != null
    }
}
