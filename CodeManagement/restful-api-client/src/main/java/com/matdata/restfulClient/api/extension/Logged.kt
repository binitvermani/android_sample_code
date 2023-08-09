package com.matdata.restfulClient.api.extension

import android.util.Log
import com.matdata.restfulClient.BuildConfig

fun log(title: String, message: String) {
    if (BuildConfig.DEBUG) {
        Log.e(title, message)
    }
}

fun handleException(e: Exception) {
    if (BuildConfig.DEBUG) {
        e.printStackTrace()
    }
}