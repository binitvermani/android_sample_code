
package com.matdata.ui.activity


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import com.matdata.R
import com.matdata.databinding.ActivityLoginBinding
import com.matdata.ui.base.BaseActivity
import com.matdata.ui.extensions.replaceFragment
import com.matdata.ui.extensions.replaceFragmentNoStack
import com.matdata.ui.fragment.ActivationCodeFragment
import com.matdata.ui.fragment.LoginFragment

class LoginSignUpActivity : BaseActivity() {

    var binding : ActivityLoginBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login)
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        binding!!.appABL.outlineProvider = null
        replaceFragment(LoginFragment(), R.id.login_frame)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.login_frame)
        when {
            fragment is LoginFragment-> {
                backAction()
            }
            supportFragmentManager.backStackEntryCount > 0 -> {
                supportFragmentManager.popBackStack()
            }
            else -> {
                replaceFragmentNoStack(LoginFragment(), R.id.login_frame)
            }
        }
    }

    fun setToolbar(titleName: String, tittle: Boolean, isShow: Boolean) {
        setSupportActionBar(binding!!.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        if (tittle) {
            binding!!.titleTV.visibility = View.VISIBLE
            binding!!.titleTV.text = titleName
        } else {
            binding!!.titleTV.visibility = View.GONE
        }
        if (isShow) {
            binding!!.appABL.visibility = View.VISIBLE
        } else {
            binding!!.appABL.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (supportActionBar != null) {
            when (supportFragmentManager.findFragmentById(R.id.login_frame)) {
                is LoginFragment, is ActivationCodeFragment -> {
                    supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                }
                else -> {
                    supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_back)
                }
            }
        }
        return true
    }
}
