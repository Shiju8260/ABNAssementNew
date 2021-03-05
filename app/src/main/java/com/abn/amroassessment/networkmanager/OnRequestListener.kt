package com.staralliance.networkframework

import okhttp3.Request

interface OnRequestListener {
    fun onRequestListener(path: String, builder: Request.Builder): Request.Builder?
}