package com.matdata.ui.dialog

import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.matdata.R
import com.matdata.databinding.DialogLogoutBinding
import com.matdata.ui.base.BaseActivity

class LogoutDialog(var context: BaseActivity, var clickDialog: OnDialogClickListener) : BaseDialog(context,R.style.CustomDialog){

    private var binding: DialogLogoutBinding? = null
    init {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_logout, null, false)
        window!!.setWindowAnimations(R.style.CustomDialog)
        setContentView(binding!!.root)
        setCancelable(true)
//        binding!!.logoutBT.setOnClickListener {
//            dismiss()
//            clickDialog.onDialogClick()
//        }
//        binding!!.crossIB.setOnClickListener {
//            dismiss()
//        }
    }
}