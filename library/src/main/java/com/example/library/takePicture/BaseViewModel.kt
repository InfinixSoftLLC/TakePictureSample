package com.example.library.takePicture

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.content.Context
import java.lang.ref.WeakReference

open class BaseViewModel : ViewModel() {
    protected var contextRef: WeakReference<Context>? = null
    private val showMessageLiveData = MutableLiveData<String>()

    fun getShowMessageLiveData() = showMessageLiveData

    fun showMessage(message: String?) = showMessageLiveData.postValue(message)

    open fun init(context: Context) {
        contextRef = WeakReference(context)
    }
}