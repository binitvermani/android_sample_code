
package com.matdata.restfulClient.api

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


interface SyncEventListener {

    fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?)

    fun onSyncFailure(errorCode: Int, t: Throwable?, response: Response<String>?, call: Call<String>?, callBack: Callback<String>?)

    fun onSyncStart()

    fun onSyncFinish()


}