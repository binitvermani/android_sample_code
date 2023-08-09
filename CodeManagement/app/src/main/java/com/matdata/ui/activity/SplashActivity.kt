
package com.matdata.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import com.matdata.R
import com.matdata.databinding.ActivitySplashBinding
import com.matdata.ui.base.BaseActivity
import com.matdata.utils.Const

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private var binding : ActivitySplashBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideStatusBar()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        initUI()
    }

    private fun initUI() {

        val topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        binding!!.imageLogo.animation = topAnim
        binding!!.textBig.animation = bottomAnim
        binding!!.textSmall.animation = bottomAnim

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ initFCM() }, Const.SPLASH_TIMEOUT.toLong())
    }

}
