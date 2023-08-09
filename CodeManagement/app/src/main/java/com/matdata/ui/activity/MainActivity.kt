
package com.matdata.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.matdata.R
import com.matdata.databinding.ActivityMainBinding
import com.matdata.ui.base.BaseActivity
import com.matdata.ui.extensions.replaceFragmentNoStack
import com.matdata.ui.fragment.HomeFragment


class MainActivity : BaseActivity(){
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        init()
    }

    private fun init() {
        updateStatusBarColor()
        gotoToHomeFragment()
    }

    private fun gotoToHomeFragment() {
        replaceFragmentNoStack(HomeFragment(), R.id.frame_container)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.frame_container)
        when {
            fragment is HomeFragment  -> {
                backAction()
            }
            supportFragmentManager.backStackEntryCount > 0 -> {
                supportFragmentManager.popBackStack()
            }
        }
    }
}
