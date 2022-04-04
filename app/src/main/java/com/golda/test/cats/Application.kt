package com.golda.test.cats

import android.app.Application
import android.content.Context
import com.golda.test.cats.di.AppComponent
import com.golda.test.cats.di.DaggerAppComponent


class NewsApplication : Application() {

    private var _appComponent: AppComponent? = null

    val appComponent: AppComponent
        get() = checkNotNull(_appComponent)

    override fun onCreate() {
        super.onCreate()
        _appComponent = DaggerAppComponent.builder()
            .application(this)
            .create()
    }
}

val Context.appComponent: AppComponent
    get() = when (this) {
        is NewsApplication -> appComponent
        else -> (applicationContext as NewsApplication).appComponent
    }