package com.abn.assessment.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.abn.amroassessment.model.venuesearchresponse.Venue
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {
    private var job = Job()
    var coroutineContext: CoroutineContext = job + Dispatchers.Main
    private var coroutineScope: CoroutineScope = CoroutineScope(coroutineContext)

    private val mProgressBarVisibility: MutableLiveData<Boolean> = MutableLiveData()
    val mProgressBarVisibilityLiveData: LiveData<Boolean> by lazy { mProgressBarVisibility }


    override fun onCleared() {
        super.onCleared()
        coroutineContext.cancel()
    }

    fun getCoroutineScope(): CoroutineScope {
        if (!coroutineScope.isActive) {
            job = Job()
            coroutineContext = job + Dispatchers.Main
            coroutineScope = CoroutineScope(context = coroutineContext)
        }
        return coroutineScope
    }

    fun showProgress(){
        mProgressBarVisibility.value = true
    }

    fun dismissProgress(){
        mProgressBarVisibility.value = false
    }

}