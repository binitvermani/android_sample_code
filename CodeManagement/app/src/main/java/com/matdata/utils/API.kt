
package com.matdata.utils

import retrofit2.Call
import retrofit2.http.*

interface API {

    /*Authentication*/
    @FormUrlEncoded
    @POST(Const.API_SEND_OTP)
    fun apiOTPSend(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.API_VERIFY_OTP)
    fun apiOTPVerify(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.API_CHANGE_PASSWORD)
    fun hitChangePassword(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.API_FORGOT_PASSWORD)
    fun apiForgotPassWord(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @FormUrlEncoded
    @POST(Const.API_UPDATE_PROFILE)
    fun hitUpdateUser(@FieldMap serverHashMap: HashMap<String, Any?>?): Call<String>

    @GET(Const.API_GET_PAGE)
    fun apiGetPage(@Query("type_id") type: Int): Call<String>

    @GET(Const.API_LOGOUT)
    fun apiLogout(): Call<String>

}