package com.tt.it_dictionary

import android.app.Application
import com.tt.it_dictionary.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ITDictionaryApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ITDictionaryApp)
            androidLogger()
            modules(appComponent)
        }
    }
}