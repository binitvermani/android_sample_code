
package com.matdata.ui.fragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.matdata.R
import com.matdata.databinding.FragmentLoginBinding
import com.matdata.ui.activity.LoginSignUpActivity
import com.matdata.ui.base.BaseFragment
import com.matdata.ui.extensions.replaceFragWithArgs
import com.matdata.ui.extensions.replaceFragmentNoStack
import com.matdata.utils.ClickHandler
import com.matdata.utils.Const
import com.matdata.utils.extensions.setColor
import com.matdata.viewmodel.LoginViewModel
import java.net.URLEncoder
import java.util.*
import kotlin.collections.ArrayList


class LoginFragment : BaseFragment(), ClickHandler, TextWatcher {

    private var binding: FragmentLoginBinding? = null
    private var isButtonActive : Boolean? = false
    lateinit var viewModel: LoginViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (baseActivity as LoginSignUpActivity).setToolbar("", tittle = false, isShow = false)
        if(binding == null) {
            binding = FragmentLoginBinding.inflate(inflater, container, false)
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
        binding!!.handleClick = this
        binding!!.mobileNumberET.addTextChangedListener(this)
        binding!!.mobileNumberET.requestFocus()
    }

    override fun onHandleClick(vararg objects: Any) {
        if(objects.isNotEmpty()){
            val view = objects[0] as View
            when (view.id) {
                R.id.getOtpBT -> {
                    when {
                        isButtonActive!! -> {
                            viewModel.mobileNumber.value = binding!!.countryCodeTV.text.toString() + binding!!.mobileNumberET.text.toString()
                            viewModel.hitSendOtpApi()
                        }
                        else -> {
                            showToast(baseActivity!!.getString(R.string.please_enter_valid_mobile_number))
                        }
                    }
                }
            }

        }
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        when (binding!!.mobileNumberET.text?.length) {
            10 -> {
                isButtonActive = true
                binding!!.getOtpBT.background = ContextCompat.getDrawable(baseActivity!!,R.drawable.bg_active_button)
                binding!!.getOtpBT.setColor(R.color.White)
            }
            else -> {
                isButtonActive = false
                binding!!.getOtpBT.background = ContextCompat.getDrawable(baseActivity!!,R.drawable.bg_inactive_button)
                binding!!.getOtpBT.setColor(R.color.Black)
            }
        }
    }

    override fun afterTextChanged(p0: Editable?) {
    }

    override fun onApiSuccess(vararg items: Any) {
        super.onApiSuccess(*items)
        when(items[0] as String){
            Const.API_SEND_OTP -> {
                showToast(baseActivity!!.getString(R.string.an_otp_will_be_send_on_the_mentioned_mob_number_to_verify_the_user))
                val bundle = Bundle()
                bundle.putString("phone_number",viewModel.mobileNumber.value)
                baseActivity!!.replaceFragWithArgs(
                    ActivationCodeFragment(),
                    R.id.login_frame,
                    bundle
                )
            }
        }
    }

    private fun sendMessageToWhatsAppContact() {

        val mylist: ArrayList<String> = arrayListOf()
        mylist.add("+918196002808")
        mylist.add("+919780986763")
        val data = mylist.toList().joinToString(";", "smsto:")

        val packageManager: PackageManager = baseActivity!!.packageManager
        val i = Intent(Intent.ACTION_VIEW)
        try {
            val url =
                "https://api.whatsapp.com/send?phone=" + Uri.parse(data) + "&text=" + URLEncoder.encode(
                    "Hello",
                    "UTF-8"
                )
            i.setPackage("com.whatsapp")
            i.data = Uri.parse(url)
            if (i.resolveActivity(packageManager) != null) {
                baseActivity!!.startActivity(i)
            }else{
                showToastOne("not send")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


//        val mylist: ArrayList<String> = arrayListOf()
//        mylist.add("+918196002808")
//        mylist.add("+919780986763")
//        val data = mylist.toList().joinToString(";", "smsto:")
//
//        val sendIntent = Intent(Intent.ACTION_VIEW)
//        sendIntent.setPackage("com.whatsapp")
//        val url =
//            "https://api.whatsapp.com/send?text=" + URLEncoder.encode(
//                "Hello",
//                "UTF-8"
//            )
//        sendIntent.data = Uri.parse(data)
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "hello")
//
//        if (sendIntent.resolveActivity(baseActivity!!.packageManager) == null) {
//            showToastOne("you should install whatsapp.")
//            return
//        }
//
//        startActivity(sendIntent)
    }
}
