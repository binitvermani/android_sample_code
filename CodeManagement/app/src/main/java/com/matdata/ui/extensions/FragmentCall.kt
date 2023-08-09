

package com.matdata.ui.extensions

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.matdata.R


fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.inTransaction {
        setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
        replace(frameId, fragment).addToBackStack(fragment.javaClass.name)
    }
}

fun AppCompatActivity.replaceFragWithArgs(fragment: Fragment, frameId: Int, args: Bundle) {
    fragment.arguments = args
    supportFragmentManager.inTransaction {
        setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
        replace(frameId, fragment).addToBackStack(fragment.javaClass.name)
    }
}

fun AppCompatActivity.replaceFragmentNoStack(fragment: Fragment, frameId: Int, args: Bundle? = null) {
    fragment.arguments = args
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
    }
}

fun AppCompatActivity.replaceFragWithArgsNoStack(fragment: Fragment, frameId: Int, args: Bundle) {
    fragment.arguments = args
    supportFragmentManager.inTransaction {
        replace(frameId, fragment)
    }
}

fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int, backStackTag: String? = null, args: Bundle? = null) {
    fragment.arguments = args
    supportFragmentManager.inTransaction {
        add(frameId, fragment)
        backStackTag?.let {
            addToBackStack(fragment.javaClass.name)
        }!!
    }
}

fun <T> T.serializeToMap(): Map<String, Any?> {
    return convert()
}
//convert an object of type I to type O
inline fun <I, reified O> I.convert(): O {

    val json = Gson().toJson(this)
    return Gson().fromJson(json, object : TypeToken<O>() {}.type)
}