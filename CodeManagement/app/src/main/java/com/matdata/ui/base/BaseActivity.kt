
package com.matdata.ui.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.os.*
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.FirebaseApp
import com.matdata.BuildConfig
import com.matdata.R
import com.matdata.restfulClient.api.*
import com.matdata.ui.activity.LoginSignUpActivity
import com.matdata.ui.activity.MainActivity
import com.matdata.ui.snackBar.ActionClickListener
import com.matdata.ui.snackBar.Snackbar
import com.matdata.ui.snackBar.SnackbarManager
import com.matdata.ui.snackBar.SnackbarType
import com.matdata.utils.*
import com.matdata.viewmodel.BaseNavigator
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
open class BaseActivity : AppCompatActivity(), SyncEventListener, View.OnClickListener, BaseNavigator {

    private var gpsAlert: AlertDialog? = null
    var restFullClient: RestFullClient? = null
    var api: API? = null
    var apiInstance: Retrofit? = null

    var inflater: LayoutInflater? = null
    var store: PrefStore? = null
    var permCallback: PermCallback? = null
    private var progressDialog: Dialog? = null
    private var txtMsgTV: TextView? = null
    private var reqCode: Int = 0
    private var networksBroadcast: NetworksBroadcast? = null
    private val networkAlertDialog: AlertDialog? = null
    private var networkStatus: String? = null
    private var inputMethodManager: InputMethodManager? = null
    private var failureDailog: android.app.AlertDialog.Builder? = null
    private var failureAlertDialog: android.app.AlertDialog? = null
    private var exit: Boolean = false
    private var fireBaseInstance: FirebaseApp? = null
    var isEmployeeViewActive : Boolean = false

    val deviceToken: String?
        get() = if (store!!.getString(Const.DEVICE_TOKEN, "")!!.isEmpty()) {
            uniqueDeviceId
        } else {
            store!!.getString(Const.DEVICE_TOKEN)
        }


    val uniqueDeviceId: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

    val isNetworkAvailable: Boolean
        @SuppressLint("MissingPermission")
        get() {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetworkInfo = connectivityManager
                    .activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting
        }

    private fun getToken() {


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fireBaseInstance = FirebaseApp.initializeApp(this)
        inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        restFullClient = RestFullClient.getInstance(this)
        apiInstance = restFullClient!!.getRetrofitInstance(Const.SERVER_REMOTE_URL)
        api = apiInstance!!.create(API::class.java)

        this@BaseActivity.overridePendingTransition(R.anim.fade_in,
                R.anim.fade_out)
        inputMethodManager = this
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        store = PrefStore(this)
        initializeNetworkBroadcast()
        store?.saveString(Const.DEVICE_TOKEN, uniqueDeviceId)

        strictModeThread()
        progressDialog()
        failureDailog = android.app.AlertDialog.Builder(this)
        if (fireBaseInstance != null) {
            getToken()
        }

    }

    fun initFCM() {
        when {
            restFullClient!!.getLoginStatus() != null -> {
                gotoMainActivity()
            }
            else -> {
                gotoLoginSignUpActivity()
            }
        }
    }

    fun gotoLoginSignUpActivity() {
        startActivity(Intent(this, LoginSignUpActivity::class.java))
        finish()
    }

    private fun initializeNetworkBroadcast() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION)
        networksBroadcast = NetworksBroadcast()
        registerReceiver(networksBroadcast, intentFilter)
    }

    private fun unregisterNetworkBroadcast() {
        try {
            if (networksBroadcast != null) {
                unregisterReceiver(networksBroadcast)
            }
        } catch (e: IllegalArgumentException) {
            networksBroadcast = null
        }
    }

    private fun showNoNetworkDialog(status: String?) {
        networkStatus = status
        if (SnackbarManager.currentSnackbar != null) {
            SnackbarManager.currentSnackbar!!.dismiss()
        }
        SnackbarManager.create(
                Snackbar.with(this)
                        .type(SnackbarType.SINGLE_LINE)
                        .text(status!!).duration(Snackbar
                                .SnackbarDuration.LENGTH_INDEFINITE)
                        .actionLabel(getString(R.string.retry_caps))
                        .actionListener(object : ActionClickListener {
                            override fun onActionClicked(snackbar: Snackbar) {
                                if (!isNetworkAvailable) {
                                    showNoNetworkDialog(networkStatus)
                                } else
                                    SnackbarManager.currentSnackbar!!.dismiss()
                            }
                        }))!!.show()
    }

    fun changeDateFormat(dateString: String?, sourceDateFormat: String, targetDateFormat: String): String {
        if (dateString == null || dateString.isEmpty()) {
            return ""
        }
        val inputDateFormat = SimpleDateFormat(sourceDateFormat, Locale.getDefault())
        var date = Date()
        try {
            date = inputDateFormat.parse(dateString)
        } catch (e: ParseException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }

        val outputDateFormat = SimpleDateFormat(targetDateFormat, Locale.getDefault())
        return outputDateFormat.format(date)
    }

    fun changeDateFormatFromDate(sourceDate: Date?, targetDateFormat: String?): String {
        if (sourceDate == null || targetDateFormat == null || targetDateFormat.isEmpty()) {
            return ""
        }
        val outputDateFormat = SimpleDateFormat(targetDateFormat, Locale.getDefault())
        return outputDateFormat.format(sourceDate)
    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, Const.PLAY_SERVICES_RESOLUTION_REQUEST)
                    ?.show()
            } else {
                log(getString(R.string.this_device_is_not_supported))
                finish()
            }
            return false
        }
        return true
    }

    fun checkPermissions(perms: Array<String>, requestCode: Int, permCallback: PermCallback): Boolean {
        this.permCallback = permCallback
        this.reqCode = requestCode
        val permsArray = ArrayList<String>()
        var hasPerms = true
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                permsArray.add(perm)
                hasPerms = false
            }
        }
        return if (!hasPerms) {
            val permsString = arrayOfNulls<String>(permsArray.size)
            for (i in permsArray.indices) {
                permsString[i] = permsArray[i]
            }
            ActivityCompat.requestPermissions(this@BaseActivity, permsString, Const.PERMISSION_ID)
            false
        } else
            true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        var permGrantedBool = false
        when (requestCode) {
            Const.PERMISSION_ID -> {
                for (grantResult in grantResults) {
                    if (grantResult == PackageManager.PERMISSION_DENIED) {
                        showToast(getString(R.string.not_sufficient_permissions)
                                + getString(R.string.app_name)
                                + getString(R.string.permissionss))
                        permGrantedBool = false
                        break
                    } else {
                        permGrantedBool = true
                    }
                }
                if (permCallback != null) {
                    if (permGrantedBool) {
                        permCallback!!.permGranted(reqCode)
                    } else {
                        permCallback!!.permDenied(reqCode)
                    }
                }
            }
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun exitFromApp() {
        finish()
        finishAffinity()
    }

    fun hideSoftKeyboard(): Boolean {
        try {
            if (currentFocus != null) {
                inputMethodManager!!.hideSoftInputFromWindow(this.currentFocus!!.windowToken, 0)
                return true
            }
        } catch (e: Exception) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }

        return false
    }

    fun isValidMail(email: String): Boolean {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())
    }

    fun isValidPassword(password: String): Boolean {
        return password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[!&^%$#@()=*/.+_-])(?=\\S+$).{8,}$".toRegex())
    }

    fun isValidName(name: String) : Boolean{
        return name.matches("^[A-Za-z]+\$".toRegex())
    }

    fun keyHash() {
        try {
            val info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                if (BuildConfig.DEBUG) {
                    Log.e("KeyHash:>>>>>>>>>>>>>>>", "" + Base64.encodeToString(md.digest(), Base64.DEFAULT))
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        } catch (e: NoSuchAlgorithmException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    fun log(string: String) {
       if (BuildConfig.DEBUG) {
            Log.e("BaseActivity", string)
    }
    }

    fun log(title: String, message: String?) {
        if (BuildConfig.DEBUG) {
            Log.e(title, message ?: "")
        }
    }

    private fun progressDialog() {
        progressDialog = Dialog(this)
        val view = View.inflate(this, R.layout.progress_dialog, null)
        progressDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressDialog!!.setContentView(view)
        progressDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        txtMsgTV = view.findViewById<View>(R.id.txtMsgTV) as TextView
        progressDialog!!.setCancelable(false)
    }

    fun startProgressDialog() {
        if (progressDialog != null && !progressDialog!!.isShowing) {
            try {
                progressDialog!!.show()
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun stopProgressDialog() {
        if (progressDialog != null && progressDialog!!.isShowing) {
            try {
                progressDialog!!.dismiss()
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onSyncStart() {
        startProgressDialog()

    }

    override fun onSyncFinish() {
        stopProgressDialog()
    }

    open fun errorSnackBar(errorString: String, call: Call<String>?, callBack: Callback<String>?): SnackbarManager? {
        val buttonText: String = if (call != null && callBack != null) {
            getString(R.string.retry_cap)
        } else {
            getString(R.string.exit_caps)
        }
        val snackBar: Snackbar = Snackbar.with(this)
                .type(SnackbarType.MULTI_LINE)
                .text(errorString)
                .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                .actionLabel(buttonText)
                .actionListener(object : ActionClickListener {
                    override fun onActionClicked(snackbar: Snackbar) {
                        if (call != null && callBack != null) {
                            onSyncStart()
                            call.clone().enqueue(callBack)
                        } else {
                            finish()
                        }
                    }
                })
        return SnackbarManager.create(snackBar)
    }


    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
        try {
            val jsonObject = JSONObject(response!!)
            when {
                responseUrl.contains(Const.API_LOGOUT) -> {
                    if (responseCode == Const.STATUS_OK) {
                        logoutClearData()
//                        showToast(getString(R.string.logout_successfully))
                    }
                }
            }

        } catch (e: JSONException) {
            if (BuildConfig.DEBUG) {
                e.printStackTrace()
            }
        }
    }

    override fun onSyncFailure(errorCode: Int, t: Throwable?, response: Response<String>?, call: Call<String>?, callBack: Callback<String>?) {
        log("error_message", if (response != null) response.message() else "")
        log("error_code", errorCode.toString())
        if (this.isFinishing) return
        if (failureAlertDialog != null && failureAlertDialog!!.isShowing) {
            failureAlertDialog!!.dismiss()
        }
        /*errorCode == HTTPS_RESPONSE_CODE.FORBIDDEN_ERROR || */
        if (errorCode == HTTPS_RESPONSE_CODE.UN_AUTHORIZATION) {
            log(getString(R.string.error), getString(R.string.session_timeout_redirecting))
            showToast(getString(R.string.session_timeout_redirecting))
            logoutClearData()
        } else if (errorCode == HTTPS_RESPONSE_CODE.SERVER_ERROR) {
            showToast(getString(R.string.problem_connecting_to_the_server))
        } else if (t is SocketTimeoutException || t is ConnectException) {
            log(getString(R.string.error), getString(R.string.request_timeout_slow_connection))
            errorSnackBar(getString(R.string.request_timeout_slow_connection), call, callBack)!!.show()
        } else if (t is AppInMaintenance) {
            log(getString(R.string.error), getString(R.string.api_is_in_maintenance))
            failureErrorDialog(t.message!!, call, callBack)!!.show()
        } else if (t is AppExpiredError) {
            log(getString(R.string.error), getString(R.string.api_is_expired))
        } else {
            log(getString(R.string.error), if (response != null) response.message() else if (t != null) t.message else "")
            var message = getString(R.string.problem_connecting_to_the_server)
            try {
                val json = JSONObject(response?.body()
                        ?: response?.errorBody()?.string() ?: "{'message':'$message'}")
                if (json.has("message")) message = json.getString("message") else if (json.has("error")) message = json.getString("error")
            } catch (e: java.lang.Exception) {
                handelException(e)
            }
            showToast(message)
        }
    }

    private fun failureErrorDialog(errorString: String, call: Call<String>?, callBack: Callback<String>?): android.app.AlertDialog? {
        if (call != null && callBack != null) {
            failureDailog!!.setMessage(errorString).setCancelable(false).setNegativeButton(getString(R.string.exit_caps)) { dialog, which -> finish() }.setPositiveButton(getString(R.string.retry_cap)) { dialog, which ->
                onSyncStart()
                call.clone().enqueue(callBack)
            }
        } else failureDailog!!.setMessage(errorString).setCancelable(false).setPositiveButton(getString(R.string.exit_caps)) { dialog, which -> finish() }
        failureAlertDialog = failureDailog!!.create()
        return failureAlertDialog
    }


    fun showToast(msg: String) {
        hideSoftKeyboard()
        SnackbarManager.create(
                Snackbar.with(this).duration(Snackbar.SnackbarDuration.LENGTH_SHORT)
                        .type(SnackbarType.MULTI_LINE)
                        .text(msg))!!.show()
    }

    fun showToastOne(msg: String) {
        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()

    }

    private fun strictModeThread() {
        val policy = StrictMode.ThreadPolicy.Builder()
                .permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    private fun transitionSlideInHorizontal() {
        this.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left)
    }

    override fun onClick(v: View) {

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterNetworkBroadcast()
    }

    fun backAction() {
        if (exit) {
            finishAffinity()
        } else {
            showToast(getString(R.string.press_one_more_time))
            exit = true
            Handler(Looper.getMainLooper()).postDelayed({ exit = false }, (2 * 1000).toLong())
        }
    }

    interface PermCallback {
        fun permGranted(resultCode: Int)

        fun permDenied(resultCode: Int)
    }

    inner class NetworksBroadcast : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val status = NetworkUtil.getConnectivityStatusString(context)
            if (status == null && networkAlertDialog != null) {
                networkStatus = null
                networkAlertDialog.dismiss()
            } else if (status != null && networkStatus == null)
                showNoNetworkDialog(status)
        }
    }

    open fun handelException(e: java.lang.Exception) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
    }

    fun getStringValue(string: Int): String {
        return resources.getString(string)
    }

    fun gotoMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onError(message: String) {
        showToastOne(message)
    }

    override fun onApiError(errorCode: Int, t: Throwable?, response: Response<String>?, call: Call<String>?, callBack: Callback<String>?) {
        onSyncFailure(errorCode, null, response, call, callBack)
    }

    override fun onStartProgress() {
        onSyncStart()
    }

    override fun onFinishProgress() {
        onSyncFinish()
    }

    override fun onApiSuccess(vararg items: Any) {

    }

    fun hitLogOutApi() {
        val call = api!!.apiLogout()
        restFullClient!!.sendRequest(call, this)
    }

    fun logoutClearData() {
//        store!!.saveProfileDataInPrefStore(null)
        restFullClient!!.setLoginStatus(null)
        gotoLoginSignUpActivity()
    }

    private fun isToday(timestamp: Long): Boolean {
        val now = Calendar.getInstance()
        val timeToCheck = Calendar.getInstance()
        timeToCheck.timeInMillis = timestamp
        return (now[Calendar.YEAR] === timeToCheck[Calendar.YEAR]
                && now[Calendar.DAY_OF_YEAR] === timeToCheck[Calendar.DAY_OF_YEAR])
    }

    fun hideStatusBar(){
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    fun getPoppinMediumFont(): Typeface {
        return ResourcesCompat.getFont(this, R.font.poppins_medium)!!
    }

    fun clearStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun clearNotification() {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.cancelAll()
    }

    override fun onResume() {
        super.onResume()
        clearNotification()
    }

    fun updateStatusBarColor(){
        val window = window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this,R.color.colorPrimary)
    }
}