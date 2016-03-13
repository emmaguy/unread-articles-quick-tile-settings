package com.emmaguy.quicktilepocket.feature.main

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody
import retrofit2.adapter.rxjava.Result
import retrofit2.http.*
import rx.Observable

interface PocketAuthApi {
    @POST("v3/oauth/request")
    @Headers("Content-Type: application/json", "X-Accept: application/json")
    fun requestToken(@Body body: RequestBody): Observable<Result<RequestToken>>

    @POST("v3/oauth/authorize")
    @Headers("Content-Type: application/json", "X-Accept: application/json")
    fun accessToken(@Body body: RequestBody): Observable<Result<AccessToken>>
}

data class RequestToken(val code: String) {

}

data class AccessToken(@SerializedName("access_token") val accessToken: String, val username: String) {

}
