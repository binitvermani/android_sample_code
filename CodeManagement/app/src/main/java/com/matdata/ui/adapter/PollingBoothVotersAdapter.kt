package com.matdata.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.matdata.R
import com.matdata.databinding.AdapterPollingBoothBinding
import com.matdata.databinding.AdapterPollingBoothVotersBinding
import com.matdata.databinding.AdapterUpdateSyncBinding
import com.matdata.databinding.AdapterVoterSearchBinding
import com.matdata.ui.base.BaseActivity


class PollingBoothVotersAdapter(private var baseActivity: BaseActivity) : BaseAdapter() {

    private var binding: AdapterPollingBoothVotersBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_polling_booth_voters, parent, false)
        return BaseViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        binding = holder.binding as AdapterPollingBoothVotersBinding


    }

    override fun getItemCount(): Int = 10
}