package com.example.mycontact

import android.app.Application
import com.androidnetworking.AndroidNetworking


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidNetworking.initialize(applicationContext)
    }

}