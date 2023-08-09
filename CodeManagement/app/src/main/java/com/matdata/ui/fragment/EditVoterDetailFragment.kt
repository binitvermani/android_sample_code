package com.matdata.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matdata.R
import com.matdata.databinding.FragmentActivationCodeBinding
import com.matdata.databinding.FragmentEditVoterDetailBinding
import com.matdata.databinding.FragmentSearchForPrintBinding
import com.matdata.databinding.FragmentSettingsBinding
import com.matdata.ui.activity.LoginSignUpActivity
import com.matdata.ui.base.BaseFragment
import com.matdata.utils.ClickHandler


class EditVoterDetailFragment : BaseFragment(), ClickHandler {

    private var binding: FragmentEditVoterDetailBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(binding == null) {
            binding = FragmentEditVoterDetailBinding.inflate(inflater, container, false)
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding!!.handleClick = this
    }

    override fun onHandleClick(vararg objects: Any) {
        if(objects.isNotEmpty()){
            val view = objects[0] as View
            when (view.id) {
            }

        }
    }

}
