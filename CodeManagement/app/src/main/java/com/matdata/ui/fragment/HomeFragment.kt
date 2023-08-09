package com.matdata.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.matdata.R
import com.matdata.databinding.FragmentHomeBinding
import com.matdata.ui.base.BaseFragment
import com.matdata.ui.extensions.replaceFragment
import com.matdata.utils.ClickHandler


class HomeFragment : BaseFragment(), ClickHandler {

    private var binding: FragmentHomeBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (binding == null) {
            binding = FragmentHomeBinding.inflate(inflater, container, false)
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
        if (objects.isNotEmpty()) {
            val view = objects[0] as View
            when (view.id) {
                R.id.settingIV -> {
                    baseActivity!!.replaceFragment(SettingsFragment(),R.id.frame_container)
                }
                R.id.voteSearchCV -> {
                    baseActivity!!.replaceFragment(VoterSearchFragment(),R.id.frame_container)
                }
                R.id.printCV -> {
                    baseActivity!!.replaceFragment(SearchForPrintFragment(),R.id.frame_container)
                }
                R.id.contactNumberCV -> {
                    baseActivity!!.replaceFragment(ContactSMSFragment(),R.id.frame_container)
                }
                R.id.casteDataCV -> {
                    baseActivity!!.replaceFragment(CastDataFragment(),R.id.frame_container)
                }
                R.id.voteCV -> {
                    baseActivity!!.replaceFragment(PollingBoothFragment(),R.id.frame_container)
                }
                R.id.updateCV -> {
                    baseActivity!!.replaceFragment(UpdateSyncFragment(),R.id.frame_container)
                }

            }

        }
    }

}
