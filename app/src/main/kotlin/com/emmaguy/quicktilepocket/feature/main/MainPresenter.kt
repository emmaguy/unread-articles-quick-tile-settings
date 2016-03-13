package com.emmaguy.quicktilepocket.feature.main

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import com.emmaguy.quicktilepocket.base.AbstractPresenter
import com.emmaguy.quicktilepocket.feature.RetrofitExtensions.success
import com.emmaguy.quicktilepocket.storage.UserStorage
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType
import okhttp3.RequestBody
import rx.Observable
import rx.Scheduler
import timber.log.Timber
import java.util.concurrent.TimeUnit

final class MainPresenter(val uiScheduler: Scheduler,
                          val ioScheduler: Scheduler,
                          val jobScheduler: JobScheduler,
                          val componentName: ComponentName,
                          val authApi: PocketAuthApi,
                          val gson: Gson,
                          val userStorage: UserStorage,
                          val consumerKey: String,
                          val callbackUrl: String)
: AbstractPresenter<MainPresenter.View>() {

    override fun onAttachView(view: MainPresenter.View) {
        super.onAttachView(view)

        if (!userStorage.accessToken.isNullOrEmpty() && !userStorage.username.isNullOrEmpty()) {
            view.showLoggedIn()
        } else {
            unsubscribeOnDetach(view.onBeginLogin()
                    .flatMap({ authApi.requestToken(requestTokenBody()).subscribeOn(ioScheduler) })
                    .filter { it.success() }
                    .map { it.response().body() }
                    .doOnNext({ userStorage.storeRequestToken(it.code) })
                    .observeOn(uiScheduler)
                    .subscribe({
                        val url = String.format(BROWSER_REDIRECT_URL_REQUEST_TOKEN, it.code, callbackUrl)
                        view.startBrowserForLogin(url)
                    }, { Timber.e(it, "Failed to begin login flow") }))

            unsubscribeOnDetach(view.onReturnFromBrowser()
                    .flatMap({ authApi.accessToken(accessTokenBody()).subscribeOn(ioScheduler) })
                    .filter { it.success() }
                    .map { it.response().body() }
                    .doOnNext {
                        userStorage.storeAccessToken(it.accessToken)
                        userStorage.storeRequestToken("")
                        userStorage.storeUsername(it.username)
                    }
                    .observeOn(uiScheduler)
                    .subscribe({
                        view.showLoggedIn()
                        scheduleRefresh()
                    }))
        }
    }

    private fun scheduleRefresh() {
        jobScheduler.cancel(JOB_ID_REFRESH_UNREAD_COUNT)

        val jobInfo = JobInfo.Builder(JOB_ID_REFRESH_UNREAD_COUNT, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(TimeUnit.HOURS.toMillis(1))
                .build()

        val jobId = jobScheduler.schedule(jobInfo)
        if (jobId <= 0) {
            // Failed
        }
    }

    private fun requestTokenBody() = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"),
            gson.toJson(RequestTokenHolder(consumerKey, callbackUrl)))

    private fun accessTokenBody() = RequestBody.create(MediaType.parse("application/json; charset=UTF-8"),
            gson.toJson(AccessTokenHolder(consumerKey, userStorage.requestToken)))

    interface View : AbstractPresenter.View {
        fun onBeginLogin(): Observable<Void>
        fun onReturnFromBrowser(): Observable<Void>

        fun startBrowserForLogin(url: String)

        fun showLoggedIn()
    }

    companion object {
        private val JOB_ID_REFRESH_UNREAD_COUNT = 1
        private val BROWSER_REDIRECT_URL_REQUEST_TOKEN = "https://getpocket.com/auth/authorize?request_token=%s&redirect_uri=%s&mobile=1";
    }

    private class RequestTokenHolder(consumerKey: String, callbackUrl: String) {
        @SerializedName("consumer_key") val consumerKey = consumerKey;
        @SerializedName("redirect_uri") val callbackUrl = callbackUrl;
    }

    private class AccessTokenHolder(consumerKey: String, code: String) {
        @SerializedName("consumer_key") val consumerKey = consumerKey;
        @SerializedName("code") val code = code;
    }
}