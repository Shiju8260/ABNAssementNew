package com.abn.assessment.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {
    private var job = Job()
    var coroutineContext: CoroutineContext = job + Dispatchers.Main
    private var coroutineScope: CoroutineScope = CoroutineScope(coroutineContext)

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

}