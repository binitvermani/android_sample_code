package com.matdata.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matdata.R
import com.matdata.databinding.FragmentActivationCodeBinding
import com.matdata.databinding.FragmentContactSmsBinding
import com.matdata.databinding.FragmentEditVoterDetailBinding
import com.matdata.databinding.FragmentSearchForPrintBinding
import com.matdata.databinding.FragmentSettingsBinding
import com.matdata.ui.activity.LoginSignUpActivity
import com.matdata.ui.adapter.ContactAdapter
import com.matdata.ui.base.BaseFragment
import com.matdata.utils.ClickHandler


class ContactSMSFragment : BaseFragment(), ClickHandler {

    private var binding: FragmentContactSmsBinding? = null
    private var adapter : ContactAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(binding == null) {
            binding = FragmentContactSmsBinding.inflate(inflater, container, false)
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        binding!!.handleClick = this
        setAdapter()
    }

    private fun setAdapter() {
        adapter = ContactAdapter(baseActivity!!)
        binding!!.progressRV.adapter = adapter
    }

    override fun onHandleClick(vararg objects: Any) {
        if(objects.isNotEmpty()){
            val view = objects[0] as View
            when (view.id) {
            }

        }
    }

}
