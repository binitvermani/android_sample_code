

package com.matdata.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.matdata.restfulClient.BuildConfig
import com.matdata.restfulClient.api.RestFullClient
import com.matdata.restfulClient.api.SyncEventListener
import com.matdata.utils.API
import com.matdata.utils.Const
import com.matdata.utils.PrefStore
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.ref.WeakReference

open class BaseViewModel<T>(application: Application) : AndroidViewModel(application), SyncEventListener {

    var mNavigator: WeakReference<T>? = null
    var apiInstance: Retrofit? = null

    val restFullClient: RestFullClient?
        get() = getApplication<BaseApp>().getApi()

    val store: PrefStore?
        get() = getApplication<BaseApp>().getPrefStore()


    fun getContext(): BaseApp {
        return getApplication()
    }

    fun getNavigator(): T? {
        return mNavigator?.get()
    }

    fun setNavigator(navigator: T) {
        mNavigator = WeakReference(navigator)
    }

    val api: API?
        get() = getApplication<BaseApp>().getApiList()


    fun log(message: String) {
        if (BuildConfig.DEBUG) {
            Log.e("Argus", message)
        }
    }

    fun logStack(e: Exception) {
        if (BuildConfig.DEBUG) {
            e.printStackTrace()
        }
    }

    fun handleError(jsonObject: JSONObject?) {

        if (jsonObject!!.has(Const.ERROR)) {
            (getNavigator() as BaseNavigator).onError(jsonObject.getString(Const.ERROR))
        }
    }

    override fun onSyncSuccess(responseCode: Int, responseMessage: String, responseUrl: String, response: String?) {
    }

    override fun onSyncFailure(errorCode: Int, t: Throwable?, response: Response<String>?, call: Call<String>?, callBack: Callback<String>?) {
        (getNavigator() as BaseNavigator).onApiError(errorCode, t, response, call, callBack)
    }

    override fun onSyncStart() {
        (getNavigator() as BaseNavigator).onStartProgress()
    }

    override fun onSyncFinish() {
        (getNavigator() as BaseNavigator).onFinishProgress()
    }
}