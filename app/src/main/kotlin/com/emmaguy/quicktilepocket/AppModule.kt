package com.emmaguy.quicktilepocket

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import com.emmaguy.quicktilepocket.storage.SharedPreferencesUserStorage
import com.emmaguy.quicktilepocket.storage.UserStorage
import com.google.gson.Gson
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

interface AppModule {
    val res: Resources
    val appContext: Context
    val sharedPreferences: SharedPreferences

    val uiScheduler: Scheduler
    val ioScheduler: Scheduler

    val consumerKey: String
    val callbackUrlScheme: String
    val callbackUrl: String

    val gson: Gson
    val userStorage: UserStorage
}

class AppModuleImpl(context: Context) : AppModule {
    override val res = context.resources
    override val appContext = context.applicationContext
    override val sharedPreferences = context.getSharedPreferences(res.getString(R.string.shared_pref_name), Context.MODE_PRIVATE)

    override val uiScheduler = AndroidSchedulers.mainThread()
    override val ioScheduler = Schedulers.io();

    override val callbackUrlScheme = res.getString(R.string.callback_url_scheme)
    override val callbackUrl = callbackUrlScheme + "://" + res.getString(R.string.callback_url_host)
    override val consumerKey = res.getString(R.string.pocket_consumer_key)

    override val gson = Gson()
    override val userStorage = SharedPreferencesUserStorage(sharedPreferences, res)
}