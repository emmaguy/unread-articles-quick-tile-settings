package com.emmaguy.quicktilepocket.feature

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Intent
import com.emmaguy.quicktilepocket.AppModule
import com.emmaguy.quicktilepocket.Inject
import com.emmaguy.quicktilepocket.Injector
import com.emmaguy.quicktilepocket.feature.RetrofitExtensions.success
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.Subscription
import rx.schedulers.Schedulers
import timber.log.Timber

class PocketRefreshService : JobService(), AppModule by Inject.instance {
    private var subscription: Subscription? = null

    override fun onStartJob(jobParameters: JobParameters): Boolean {
        Timber.d("onStartJob, accessToken: ${userStorage.accessToken}")

        val pocketApi = Retrofit.Builder()
                .baseUrl("https://getpocket.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PocketApi::class.java)

        subscription = pocketApi.refresh(createRefreshHolder())
                .filter { it.success() }
                .map { it.response().body() }
                .subscribeOn(Schedulers.io())
                .subscribe({
                    Timber.d("Storing new unread count: ${it.unreadCount}")
                    userStorage.storeUnreadCount(it.unreadCount)
                    appContext.startService(Intent(appContext, PocketQuickSettingsTileService::class.java))
                })

        return true
    }

    private fun createRefreshHolder() = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"),
            gson.toJson(RefreshHolder(consumerKey, userStorage.accessToken)))

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        subscription?.unsubscribe()

        return false
    }

    private class RefreshHolder(consumerKey: String, accessToken: String) {
        @SerializedName("consumer_key") val consumerKey = consumerKey;
        @SerializedName("access_token") val accessToken = accessToken;
    }
}