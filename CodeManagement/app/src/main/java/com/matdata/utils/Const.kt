
package com.matdata.utils

object Const {

    const val SERVER_REMOTE_URL = "http://13.58.128.21:3000/v1/"
    const val IMAGE_URL = ""

    const val DISPLAY_MESSAGE_ACTION = "com.matdata.DISPLAY_MESSAGE"
    const val FOREGROUND = "foreground_notification"

    const val PLAY_SERVICES_RESOLUTION_REQUEST = 1234
    const val SPLASH_TIMEOUT = 2500


    const val ERROR: String = "error"
    const val MESSAGE: String = "message"
    const val STATUS_OK: Int = 200
    const val USER_NOT_FOUND: Int = 404
    var PERMISSION_ID = 45864
    const val DEVICE_TOKEN = "device_token"

    //    Authentication module
    const val API_SEND_OTP = "login/send/otp"
    const val API_VERIFY_OTP = "login/verify/otp"
    const val API_UPDATE_PROFILE = "user/update"
    const val API_GET_PROFILE = "user/profile"
    const val API_LOGOUT = "user/logout"
    const val API_GET_USER_LIST = "admin/user/list"
    const val API_FORGOT_PASSWORD = "user/forgot-password"
    const val API_GET_PAGE = "user/get-page"
    const val API_CHANGE_PASSWORD = "auth/change_password"

}