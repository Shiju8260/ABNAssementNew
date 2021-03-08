package com.places.assessment

import android.app.Application
import com.staralliance.networkframework.NetworkManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initNetworkModule()

    }

    //NetworkManager setup for API calls
    private fun initNetworkModule() {
        NetworkManager.setBaseUrl(BuildConfig.BASE_URL)
        NetworkManager.setConnectionTimeOut(2)
        NetworkManager.initHttpLogging(true)
    }

}