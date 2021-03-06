package com.staralliance.networkframework

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object NetworkManager {
    private const val TAG = "NetworkManager"
    private const val CACHE_FOLDER_NAME = "http-cache"
    private const val HEADER_CACHE_CONTROL = "Cache-Control"
    private const val HEADER_PRAGMA = "Pragma"

    private var BASE_URL = ""
    private var authenticator: Authenticator? = null
    private var mCache: Cache? = null
    private var CONNECTION_TIME_OUT: Long = 2
    private var headers = mutableMapOf<String, String>()
    private var mRetrofit: Retrofit.Builder? = null
    private var responseHeaderListener: OnResponseHeaderListener? = null
    private var onRequestListener: OnRequestListener? = null
    private var interceptorList: MutableList<Interceptor?>? = null
    private var baseOkHttpClient = OkHttpClient()
    private var mOkHttpClient: OkHttpClient? = null
    private var mServerWithCachedOkHttpClient: OkHttpClient? = null
    private var mCachedOkHttpClient: OkHttpClient? = null
    private var isLogRequired: Boolean = false
    private var provideOfflineCacheInterceptor: Interceptor? = null
    private var provideCacheInterceptor: Interceptor? = null

    /**
     * Method to initiate the service call
     * @param baseUrl Api EndPoint URL
     * @param type Type to decide network or from cache
     * @return Retrofit Object
     */
    private fun initService(baseUrl: String): Retrofit? {
        if (mRetrofit == null) {
            mRetrofit = Retrofit.Builder()
            mRetrofit?.addConverterFactory(GsonConverterFactory.create(Gson()))
        }
        okHttpClient()?.let { mRetrofit?.client(it) }
        mRetrofit?.baseUrl(baseUrl)
        return mRetrofit?.build()
    }

    /**
     * Method to update OkHttp client for network only, Using a common reference "baseOkHttpClient" to avoid multiple instances of oKHttpClient
     * @return OkHttpClient Object
     */
    private fun okHttpClient(): OkHttpClient? {
        if (mOkHttpClient == null) {
            val httpClient = baseOkHttpClient.newBuilder()
                .connectTimeout(CONNECTION_TIME_OUT, TimeUnit.MINUTES)
                .readTimeout(CONNECTION_TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(CONNECTION_TIME_OUT, TimeUnit.MINUTES)
                .connectionPool(ConnectionPool(0, CONNECTION_TIME_OUT, TimeUnit.MINUTES))

            if (isLogRequired) {
                httpClient.addInterceptor(provideHttpLoggingInterceptor())
            }

            addDefaultInterceptors(httpClient)

            authenticator?.let { httpClient.authenticator(it) }
            addInterceptor(httpClient)
            mOkHttpClient = httpClient.build()
        }
        return mOkHttpClient
    }

    private fun addDefaultInterceptors(httpClient: OkHttpClient.Builder) {
        try {
            httpClient.addInterceptor(customHeaderInterceptor())
        } catch (e: Exception) {
            Log.d(TAG, e.message?:"")
        }
        try {
            httpClient.addInterceptor(getRequestInterceptor())
        } catch (e: Exception) {
            Log.d(TAG, e.message?:"")
        }
        try {
            httpClient.addInterceptor(getResponseHeaderInterceptor())
        } catch (e: Exception) {
            Log.d(TAG, e.message?:"")
        }
    }

    /**
     * Method to add application level headers for all the api calls
     * @return Return interceptor object
     */
    private fun customHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
            if (headers.isNotEmpty()) {
                for (header in headers) {
                    requestBuilder.header(header.key, header.value)
                }
            }
            requestBuilder.method(original.method, original.body)

            val request = requestBuilder.build()
            try {
                chain.proceed(request)
            } catch (e: Exception) {
                throw e
            }
        }
    }



    /**
     * Method to update the request with cache control in the absence of internet connection
     * <p> Last 7 days cache is maintained in the cache memory, Old cache headers need to be replaced with new cache control to avoid conflicts.
     * @return Return interceptor object
     */
    private fun provideOfflineCacheInterceptor(mContext: Context) {
        provideOfflineCacheInterceptor = Interceptor { chain ->
            var request = chain.request()

            if (!this@NetworkManager.isNetworkConnected(mContext)) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()

                request = request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build()
            }

            try {
                chain.proceed(request)
            } catch (e: Exception) {
                throw e
            }
        }
    }

    /**
     *  Method to create a directory in application cache memory , Current cache memory is set default to 10MB
     *  @param mContext Application context
     *  @return Cache directory
     */
    private fun provideCache(mContext: Context): Cache? {
        if (mCache == null) {
            try {
                mCache = Cache(
                    File(mContext.cacheDir, CACHE_FOLDER_NAME),
                    (10 * 1024 * 1024).toLong()
                ) // 10 MB
            } catch (e: Exception) {
                Log.e(TAG, "Could not create Cache!")
            }

        }
        return mCache
    }

    /**
     * Method to check internet connectivity
     * @param mContext Pass application context to access the system services
     */
     fun isNetworkConnected(mContext: Context): Boolean {
        try {
            val e = mContext.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as android.net.ConnectivityManager
            val activeNetwork = e.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        } catch (e: Exception) {
            Log.w(TAG, e.toString())
        }
        return false
    }

    /**
     * Method to update the request with cache control
     * <P> If network available,Cache will be fetched from network based on MaxAge Param in cache control.
     * <p> If network available,Last 7 days cache is maintained in the cache memory, Old cache headers need to be replaced with new cache control to avoid conflicts.
     * @return Return interceptor object
     */
    private fun provideCacheInterceptor(mContext: Context) {
        provideCacheInterceptor = Interceptor { chain ->
            try {
                val response = chain.proceed(chain.request())

                val cacheControl: CacheControl = if (this@NetworkManager.isNetworkConnected(mContext)) {
                    CacheControl.Builder()
                        .maxAge(0, TimeUnit.SECONDS)
                        .build()
                } else {
                    CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()
                }

                response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }

    private fun getRequestInterceptor(): Interceptor {
        return Interceptor { chain ->
            try {
                if (onRequestListener != null) {
                    val req = onRequestListener?.onRequestListener(
                        chain.request().url.toString(),
                        chain.request().newBuilder()
                    )?.build()
                    if (req != null) {
                        chain.proceed(req)
                    } else {
                        val request = chain.request().newBuilder().build()
                        chain.proceed(request)
                    }

                } else {
                    val request = chain.request().newBuilder().build()
                    chain.proceed(request)
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun getResponseHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            try {
                val response = chain.proceed(chain.request())
                if (responseHeaderListener != null) {
                    responseHeaderListener?.onResponseHeader(response.headers)
                }
                response.newBuilder()
                    .build()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    private fun addInterceptor(httpClint: OkHttpClient.Builder) {
        interceptorList?.let { list ->
            if (list.isNotEmpty()) {
                list.forEach {
                    it?.let {
                        httpClint.addInterceptor(it)
                    }
                }
            }
        }
    }

    fun setOnResponseHeaderListener(onResponseHeaderListener: OnResponseHeaderListener) {
        this.responseHeaderListener = onResponseHeaderListener
    }

    fun setOnRequestListener(onRequestListener: OnRequestListener) {
        this.onRequestListener = onRequestListener
    }


    /**
     * Method to clean the cache data
     */
    fun clean() {
        // Cancel Pending Request
        mOkHttpClient?.dispatcher?.cancelAll()

        // Cancel Pending Cached Request
        mCachedOkHttpClient?.dispatcher?.cancelAll()

        // Cancel Pending Cached Request
        mServerWithCachedOkHttpClient?.dispatcher?.cancelAll()

        mRetrofit = null

        try {
            mCache?.evictAll()
        } catch (e: Exception) {
            Log.e(TAG, "Error cleaning http cache")
        }

        mCache = null
    }


    fun setConnectionTimeOut(timeOutInMinutes: Long) {
        CONNECTION_TIME_OUT = timeOutInMinutes
    }

    /* *
     * using print the API request and response log
     * @param level your won Retrofit log type
     */
    fun initHttpLogging(isLogRequired: Boolean) {
        this.isLogRequired = isLogRequired
    }

    /* *
     * initialize API header and store to global level
     * @param headersData add header key and values
     */
    fun initHeader(headersData: MutableMap<String, String>?) {
        if (headersData != null && headersData.isNotEmpty()) {
            headers.putAll(headersData)
        }
    }

    fun getHeader(): MutableMap<String, String> {
        return headers
    }

    fun removeSelectedItemFromHeader(key: String) {
        if (headers.containsKey(key)) {
            headers.remove(key)
        }
    }

    fun setAuthenticator(authenticator: Authenticator) {
        this.authenticator = authenticator
    }

    fun setCustomInterceptors(interceptorList: MutableList<Interceptor?>?) {
        this.interceptorList = interceptorList
    }


    /**
     * set base url global level
     * @param baseUrl API base url
     */
    fun setBaseUrl(baseUrl: String) {
        BASE_URL = baseUrl
    }

    fun <T> getApi(act: Class<T>): T {
        return initService(BASE_URL)!!.create(act)
    }

    fun initAPICache(mContext: Context) {
        provideCache(mContext)
    }

    fun initAPIServerFirstCacheNext(mContext: Context) {
        provideCache(mContext)
        provideCacheInterceptor(mContext)
        provideOfflineCacheInterceptor(mContext)
    }
}