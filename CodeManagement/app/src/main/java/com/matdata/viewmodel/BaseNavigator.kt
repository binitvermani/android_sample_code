package com.matdata.viewmodel

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface BaseNavigator {
    fun onError(message: String)
    fun onApiError(errorCode: Int, t: Throwable?, response: Response<String>?, call: Call<String>?, callBack: Callback<String>?)
    fun onStartProgress()
    fun onFinishProgress()
    fun onApiSuccess(vararg items: Any)
}
