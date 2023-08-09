package com.matdata.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matdata.R
import com.matdata.databinding.FragmentActivationCodeBinding
import com.matdata.databinding.FragmentCastDataBinding
import com.matdata.databinding.FragmentSettingsBinding
import com.matdata.databinding.FragmentVoterSearchBinding
import com.matdata.ui.activity.LoginSignUpActivity
import com.matdata.ui.adapter.BaseAdapter
import com.matdata.ui.adapter.CastDataAdapter
import com.matdata.ui.adapter.VoterSearchAdapter
import com.matdata.ui.base.BaseFragment
import com.matdata.ui.extensions.replaceFragment
import com.matdata.utils.ClickHandler


class CastDataFragment : BaseFragment(), ClickHandler, BaseAdapter.OnItemClickListener {

    private var binding: FragmentCastDataBinding? = null
    private var adapter : CastDataAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(binding == null) {
            binding = FragmentCastDataBinding.inflate(inflater, container, false)
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
        adapter = CastDataAdapter(baseActivity!!)
        binding!!.castRV.adapter = adapter
        adapter!!.setOnItemClickListener(this)
    }

    override fun onHandleClick(vararg objects: Any) {
        if(objects.isNotEmpty()){
            val view = objects[0] as View
            when (view.id) {
            }

        }
    }

    override fun onItemClick(vararg itemData: Any) {
//        baseActivity!!.replaceFragment(EditVoterDetailFragment(),R.id.frame_container)
    }

}
