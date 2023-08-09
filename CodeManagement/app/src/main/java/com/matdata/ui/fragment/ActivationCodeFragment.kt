package com.matdata.ui.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.matdata.R
import com.matdata.databinding.FragmentActivationCodeBinding
import com.matdata.ui.activity.LoginSignUpActivity
import com.matdata.ui.base.BaseFragment
import com.matdata.ui.extensions.replaceFragmentNoStack
import com.matdata.utils.ClickHandler
import com.matdata.utils.Const
import com.matdata.utils.extensions.setColor
import com.matdata.viewmodel.LoginViewModel


class ActivationCodeFragment : BaseFragment(), ClickHandler{

    private var binding: FragmentActivationCodeBinding? = null
    lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (baseActivity as LoginSignUpActivity).setToolbar("", tittle = true, isShow = false)
        if(binding == null) {
            binding = FragmentActivationCodeBinding.inflate(inflater, container, false)
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.setNavigator(this)
        viewModel.mobileNumber.value = arguments?.getString("phone_number")
        binding!!.handleClick = this
    }

    override fun onHandleClick(vararg objects: Any) {
        if(objects.isNotEmpty()){
            val view = objects[0] as View
            when (view.id) {
                R.id.activateBT -> {
                    when {
                        binding!!.codeET.text!!.isNotEmpty() -> {
                            viewModel.otp.value = binding!!.codeET.text.toString()
                            viewModel.hitVerifyOtpApi()
                        }

                        else -> {
                            showToast(baseActivity!!.getString(R.string.please_provide_activation_code_password))
                        }
                    }
                }
            }

        }
    }

    override fun onApiSuccess(vararg items: Any) {
        super.onApiSuccess(*items)
        when(items[0] as String){
            Const.API_VERIFY_OTP -> {
                baseActivity!!.gotoMainActivity()
            }
        }
    }

}
