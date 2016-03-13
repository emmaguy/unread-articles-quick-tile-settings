package com.emmaguy.quicktilepocket

import com.emmaguy.quicktilepocket.feature.main.MainModule
import kotlin.properties.Delegates

interface Injector : AppModule, MainModule

internal class AppInjector(appModule: AppModule, mainModule: MainModule)
: Injector, AppModule by appModule, MainModule by mainModule

object Inject {
    var instance: Injector by Delegates.notNull()
}