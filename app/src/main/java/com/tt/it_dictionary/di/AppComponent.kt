package com.tt.it_dictionary.di

import android.app.Application
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import org.koin.dsl.module

val appComponent = module {
    single { providesSharedPreferences(get()) }
}

fun providesSharedPreferences(application: Application): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(application)
}