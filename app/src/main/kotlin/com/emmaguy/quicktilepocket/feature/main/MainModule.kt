package com.emmaguy.quicktilepocket.feature.main

import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.emmaguy.quicktilepocket.AppModule
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

interface MainModule {
    val mainPresenter: MainPresenter
    val authApi: PocketAuthApi
    val jobScheduler: JobScheduler
}

class MainModuleImpl(val appModule: AppModule) : MainModule {
    override val authApi = Retrofit.Builder()
            .baseUrl("https://getpocket.com/")
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PocketAuthApi::class.java)
    override val jobScheduler = appModule.appContext.getSystemService(Context.JOB_SCHEDULER_SERVICE)as JobScheduler

    override val mainPresenter = MainPresenter(appModule.uiScheduler, appModule.ioScheduler, jobScheduler,
            ComponentName(appModule.appContext, PocketRefreshService::class.java), authApi, appModule.gson,
            appModule.userStorage, appModule.consumerKey, appModule.callbackUrl)
}