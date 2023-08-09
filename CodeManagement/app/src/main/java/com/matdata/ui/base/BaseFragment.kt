
package com.matdata.ui.base

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import com.matdata.BuildConfig
import com.matdata.R
import com.matdata.restfulClient.api.RestFullClient
import com.matdata.restfulClient.api.SyncEventListener
import com.matdata.utils.API
import com.matdata.utils.Const
import com.matdata.utils.PrefStore
import com.matdata.viewmodel.BaseNavigator
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


open class BaseFragment : Fragment(), AdapterView.OnItemClickListener, View.OnClickListener, SyncEventListener, AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener, BaseNavigator {

    var baseActivity: BaseActivity? = null
    var store: PrefStore? = null
    var restFullClient: RestFullClient? = null
    var api: API? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseActivity = activity as BaseActivity?
        if (baseActivity!!.restFullClient == null) {
            baseActivity!!.restFullClient = RestFullClient.getInstance(baseActivity!!)
            baseActivity!!.apiInstance = baseActivity!!.restFullClient!!.getRetrofitInstance(Const.SERVER_REMOTE_URL)
            baseActivity!!.api = baseActivity!!.apiInstance!!.create(API::class.java)
        }
        restFullClient = baseActivity!!.restFullClient
        api = baseActivity!!.api
        store = baseActivity!!.store

    }

    override fun onResume() {
        super.onResume()
        baseActivity!!.hideSoftKeyboard()
        requireActivity().invalidateOptionsMenu()
    }

    override fun onClick(v: View) {
    }

    fun showToast(msg: String) {
        baseActivity!!.showToast(msg)
    }

    fun showToastOne(s: String) {
        baseActivity!!.showToastOne(s)
    }

    override fun onSyncStart() {
        baseActivity!!.onSyncStart()
    }

    override fun onSyncFinish() {
        baseActivity!!.onSyncFinish()
    }

    override fun onSyncFailure(errorCode: Int, t: Throwable?, response: Response<String>?, call: Call<String>?, callBack: Callback<String>?) {
        baseActivity!!.onSyncFailure(errorCode, t, response, call, callBack)
    }

    fun log(s: String) {
        baseActivity!!.log(s)
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
    }

    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

    }

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>) {

    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {

    }
    open fun elseErrorMsg(jsonObject: JSONObject) {
        var error: String? = baseActivity!!.getStringValue(R.string.something_went_wrong)
        if (jsonObject.has(Const.ERROR)) {
            error = jsonObject.optString(Const.ERROR)
        }
        showToast(error!!)
    }

    open fun successMsg(jsonObject: JSONObject) {
        var msg: String = baseActivity!!.getStringValue(R.string.success)
        when {
            jsonObject.has("message") -> {
                try {
                    msg = jsonObject.getString("message")
                } catch (e: JSONException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                }
            }
            jsonObject.has("msg") -> {
                try {
                    msg = jsonObject.getString("msg")
                } catch (e: JSONException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                }
            }
            jsonObject.has("success") -> {
                try {
                    msg = jsonObject.getString("success")
                } catch (e: JSONException) {
                    if (BuildConfig.DEBUG) {
                        e.printStackTrace()
                    }
                }
            }
        }
        if (!msg.equals(baseActivity!!.getStringValue(R.string.success), ignoreCase = true)) {
            showToast(msg)
        }
    }

    override fun onError(message: String) {
        baseActivity!!.hideSoftKeyboard()
        showToast(message)
    }

    override fun onApiError(errorCode: Int, t: Throwable?, response: Response<String>?, call: Call<String>?, callBack: Callback<String>?) {
        baseActivity!!.onApiError(errorCode, t, response, call, callBack)
    }

    override fun onStartProgress() {
        baseActivity!!.onSyncStart()
    }

    override fun onFinishProgress() {
        baseActivity!!.onSyncFinish()
    }

    override fun onApiSuccess(vararg items: Any) {
        baseActivity!!.onApiSuccess()
    }

    fun getTime(date: Date): String? {
        val format = SimpleDateFormat("dd-MM-yyyy")
        return format.format(date)
    }

    fun getFormatTime(date: Date): String? {
        val format = SimpleDateFormat("yyyy-MM-dd")
        return format.format(date)
    }

}
