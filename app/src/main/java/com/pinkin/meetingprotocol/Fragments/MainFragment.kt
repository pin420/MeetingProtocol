package com.pinkin.meetingprotocol.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pinkin.meetingprotocol.R
import com.pinkin.meetingprotocol.databinding.FragmentMainBinding
import com.pinkin.meetingprotocol.navigator

class MainFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentMainBinding.inflate(inflater, container, false)


        binding.addProtocol.setOnClickListener {

            navigator().showAddProtocol()
        }

        binding.toolbar.inflateMenu(R.menu.main_toolbar)
        binding.toolbar.setTitle(R.string.app_name)

        return binding.root
    }


}