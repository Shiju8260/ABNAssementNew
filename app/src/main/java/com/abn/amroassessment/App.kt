package com.abn.amroassessment

import android.app.Application
import com.staralliance.networkframework.NetworkManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initNetworkModule()

    }

    private fun initNetworkModule() {
        NetworkManager.setBaseUrl(BuildConfig.BASE_URL)
        NetworkManager.setConnectionTimeOut(2)
        NetworkManager.initHttpLogging(true)
    }

}