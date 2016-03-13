package com.emmaguy.quicktilepocket.feature.main

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import retrofit2.adapter.rxjava.Result
import retrofit2.http.*
import rx.Observable

interface PocketApi {
    @POST("v3/stats")
    fun refresh(@Body body: RequestBody): Observable<Result<Response>>
}

data class Response(@SerializedName("count_unread") val unreadCount: Int) {

}