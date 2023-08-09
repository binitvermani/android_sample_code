package com.matdata.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matdata.R
import com.matdata.databinding.FragmentActivationCodeBinding
import com.matdata.databinding.FragmentPollingBoothBinding
import com.matdata.databinding.FragmentSettingsBinding
import com.matdata.databinding.FragmentUpdateSyncBinding
import com.matdata.ui.activity.LoginSignUpActivity
import com.matdata.ui.adapter.BaseAdapter
import com.matdata.ui.adapter.PollingBoothAdapter
import com.matdata.ui.adapter.UpdateSyncAdapter
import com.matdata.ui.base.BaseFragment
import com.matdata.ui.extensions.replaceFragment
import com.matdata.utils.ClickHandler


class PollingBoothFragment : BaseFragment(), BaseAdapter.OnItemClickListener {

    private var binding: FragmentPollingBoothBinding? = null
    private var adapter : PollingBoothAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(binding == null) {
            binding = FragmentPollingBoothBinding.inflate(inflater, container, false)
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
        adapter = PollingBoothAdapter(baseActivity!!)
        binding!!.pollingBoothRV.adapter = adapter
        adapter!!.setOnItemClickListener(this)
    }

    override fun onItemClick(vararg itemData: Any) {
        if(itemData.isNotEmpty()){
            baseActivity!!.replaceFragment(PollingBoothVotersFragment(),R.id.frame_container)
        }
    }


}
