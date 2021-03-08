package com.staralliance.networkframework

import okhttp3.Headers

//Listener for response header web service API calls
interface OnResponseHeaderListener {
    fun onResponseHeader(headers: Headers)
}