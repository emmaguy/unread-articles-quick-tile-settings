package com.emmaguy.quicktilepocket

import android.app.Application
import com.emmaguy.quicktilepocket.feature.main.MainModuleImpl
import timber.log.Timber

class App() : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        instantiateInjector()
    }

    fun instantiateInjector() {
        val appModule = AppModuleImpl(this)
        Inject.instance = AppInjector(appModule, MainModuleImpl(appModule))
    }
}


