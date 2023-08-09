package com.matdata.viewmodel

import android.app.Application
import android.os.Build
import android.service.autofill.UserData
import androidx.annotation.RequiresApi
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.matdata.BuildConfig
import com.matdata.restfulClient.api.Api3Params
import com.matdata.utils.Const
import org.json.JSONException
import org.json.JSONObject

class LoginViewModel(application: Application) : BaseViewModel<BaseNavigator>(application) {

    val mobileNumber = MediatorLiveData<String>()
    val otp = MediatorLiveData<String>()

    var isLoading : Boolean? = true

    override fun onSyncStart() {
        if(isLoading!!) {
            super.onSyncStart()
        }
    }

    fun hitSendOtpApi() {
        val params = Api3Params()
        params.put("mobile", mobileNumber.value.toString())
        val call = api!!.apiOTPSend(params.getServerHashMap())
        restFullClient!!.sendRequest(call, this)
    }

    fun hitVerifyOtpApi() {
        val params = Api3Params()
        params.put("mobile", mobileNumber.value.toString())
        params.put("otp", otp.value.toString())
        val call = api!!.apiOTPVerify(params.getServerHashMap())
        restFullClient!!.sendRequest(call, this)
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        super.onSyncSuccess(responseCode, responseMessage, responseUrl, response)
        try {
            val jsonObject = JSONObject(response!!)
            when {
                responseUrl.contains(Const.API_SEND_OTP) -> {
                    when (responseCode) {
                        Const.STATUS_OK -> {
//                            if (jsonObject.has("data")) {
//                                val userDetail = Gson().fromJson(jsonObject.getJSONObject("data").toString(), UserData::class.java)
//                                restFullClient!!.setLoginStatus(userDetail!!.accessToken)
//                                store!!.saveProfileDataInPrefStore(userDetail)
//                                getNavigator()?.onApiSuccess(Const.API_LOGIN)
                                getNavigator()?.onApiSuccess(Const.API_SEND_OTP)
//                            } else {
//                                getNavigator()?.onError(jsonObject.getString("message"))
//                            }
                        }
                    }
                }
                responseUrl.contains(Const.API_VERIFY_OTP) -> {
                    when (responseCode) {
                        Const.STATUS_OK -> {
                            if (jsonObject.has("data")) {
//                                val userDetail = Gson().fromJson(jsonObject.getJSONObject("data").toString(), UserData::class.java)
//                                if (userDetail.flags!!.emailExist!!) {
//                                    restFullClient!!.setLoginStatus(userDetail!!.accessToken)
//                                    store!!.saveProfileDataInPrefStore(userDetail)
//                                }
//                                getNavigator()?.onApiSuccess(Const.API_CHECK_EMAIL, userDetail.flags.emailExist!!, userDetail.flags.passwordSet!!)
                                getNavigator()?.onApiSuccess(Const.API_VERIFY_OTP)
                            } else {
                                getNavigator()?.onError(jsonObject.getString("message"))
                            }
                        }
                    }
                }

                else -> {
                    handleError(jsonObject)
                }
            }

        } catch (e: JSONException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }

        }
    }

}