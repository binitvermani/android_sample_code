package com.matdata.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.matdata.R
import com.matdata.databinding.AdapterCastDataBinding
import com.matdata.databinding.AdapterContactBinding
import com.matdata.ui.base.BaseActivity


class CastDataAdapter(private var baseActivity: BaseActivity) : BaseAdapter() {

    private var binding: AdapterCastDataBinding? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.adapter_cast_data, parent, false)
        return BaseViewHolder(binding!!)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        binding = holder.binding as AdapterCastDataBinding
        binding?.root?.setOnClickListener {
            onItemClick(position)
        }

    }

    override fun getItemCount(): Int = 10
}