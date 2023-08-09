package com.matdata.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matdata.R
import com.matdata.databinding.FragmentActivationCodeBinding
import com.matdata.databinding.FragmentSettingsBinding
import com.matdata.databinding.FragmentUpdateSyncBinding
import com.matdata.ui.activity.LoginSignUpActivity
import com.matdata.ui.adapter.UpdateSyncAdapter
import com.matdata.ui.base.BaseFragment
import com.matdata.utils.ClickHandler


class UpdateSyncFragment : BaseFragment() {

    private var binding: FragmentUpdateSyncBinding? = null
    private var adapter : UpdateSyncAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(binding == null) {
            binding = FragmentUpdateSyncBinding.inflate(inflater, container, false)
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        setAdapter()
    }

    private fun setAdapter() {
        adapter = UpdateSyncAdapter(baseActivity!!)
        binding!!.pollingBoothRV.adapter = adapter
    }


}
