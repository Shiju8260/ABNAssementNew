package com.staralliance.networkframework

import okhttp3.Request

//Listener for Web service API requests thrught network manager
interface OnRequestListener {
    fun onRequestListener(path: String, builder: Request.Builder): Request.Builder?
}