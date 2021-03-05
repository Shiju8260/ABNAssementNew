package com.staralliance.networkframework

import okhttp3.Headers

interface OnResponseHeaderListener {
    fun onResponseHeader(headers: Headers)
}