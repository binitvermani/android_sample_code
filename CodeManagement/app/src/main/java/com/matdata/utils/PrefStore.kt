
package com.matdata.utils


import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson

class PrefStore(private val mAct: Context) {

    private val pref: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(mAct)

    fun cleanPref() {
        val settings = pref
        settings.edit().clear().apply()
    }

    fun containValue(key: String): Boolean {
        val settings = pref
        return settings.contains(key)
    }

    @JvmOverloads
    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
        val settings = pref
        return settings.getBoolean(key, defaultValue)
    }

    fun setBoolean(key: String, value: Boolean) {
        val settings = pref
        val editor = settings.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun saveString(key: String, value: String) {
        val settings = pref
        val editor = settings.edit()
        editor.putString(key, value)
        editor.apply()
    }

    @JvmOverloads
    fun getString(key: String, defaultVal: String? = null): String? {
        val settings = pref
        return settings.getString(key, defaultVal)
    }

    fun saveLong(key: String, value: Long) {
        val settings = pref
        val editor = settings.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    @JvmOverloads
    fun getLong(key: String, defaultVal: Long = 0): Long {
        val settings = pref
        return settings.getLong(key, defaultVal)
    }

    fun setInt(subscription_id: String, sbu_id: Int) {
        val settings = pref
        val editor = settings.edit()
        editor.putInt(subscription_id, sbu_id)
        editor.apply()
    }

    @JvmOverloads
    fun getInt(key: String, defaultVal: Int = 0): Int {
        val settings = pref
        return settings.getInt(key, defaultVal)
    }

//    fun saveProfileDataInPrefStore(profileData: UserData?) {
//        saveString(Const.PROFILE_DATA, Gson().toJson(profileData))
//    }
//
//    fun getProfileDataInPrefStore(): UserData {
//        return Gson().fromJson(getString(Const.PROFILE_DATA), UserData::class.java)
//    }


}