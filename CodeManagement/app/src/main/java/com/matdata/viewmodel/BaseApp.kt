

package com.matdata.viewmodel

import android.app.Application
import com.matdata.restfulClient.api.RestFullClient
import com.matdata.utils.API
import com.matdata.utils.Const
import com.matdata.utils.PrefStore
import retrofit2.Retrofit


class BaseApp : Application() {
    var apiInstance: Retrofit? = null
    private var restFullClient: RestFullClient? = null
    var api: API? = null
    var store: PrefStore? = null

    override fun onCreate() {
        super.onCreate()
    }

    private fun initSyncManager(): RestFullClient {
        if (restFullClient == null) {
            restFullClient = RestFullClient.getInstance(this)
            apiInstance = restFullClient!!.getRetrofitInstance(Const.SERVER_REMOTE_URL)
        }
        return restFullClient!!
    }

    fun getApi(): RestFullClient? {
        return initSyncManager()
    }

    fun getApiList(): API? {
        if (restFullClient == null) {
            restFullClient = RestFullClient.getInstance(this)
            apiInstance = restFullClient!!.getRetrofitInstance(Const.SERVER_REMOTE_URL)
            api = apiInstance!!.create(API::class.java)
        }
        return api!!
    }

    fun getPrefStore(): PrefStore {
        return initPrefStore()
    }

    private fun initPrefStore(): PrefStore {
        if (store == null) {
            store = PrefStore(this)
        }
        return store!!
    }
}