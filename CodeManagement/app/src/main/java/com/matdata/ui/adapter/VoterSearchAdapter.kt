package com.matdata.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.matdata.R
import com.matdata.databinding.AdapterVoterSearchBinding
import com.matdata.ui.base.BaseActivity


class VoterSearchAdapter(private var baseActivity: BaseActivity) : BaseAdapter() {

    private var binding: AdapterVoterSearchBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_voter_search, parent, false)
        return BaseViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        binding = holder.binding as AdapterVoterSearchBinding
        binding?.root?.setOnClickListener {
            onItemClick(position)
        }

    }

    override fun getItemCount(): Int = 10
}