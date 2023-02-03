package com.pinkin.meetingprotocol.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pinkin.meetingprotocol.ViewModel.MainViewModel
import com.pinkin.meetingprotocol.ViewModel.MainViewModelFactory
import com.pinkin.meetingprotocol.databinding.AddProtocolBinding

class AddProtocolFragment : Fragment() {

    private lateinit var vm: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = ViewModelProvider(this, MainViewModelFactory(requireContext()))[MainViewModel::class.java]

        val binding = AddProtocolBinding.inflate(inflater, container, false)


        vm.stateLive.observe(requireActivity()) { state ->
            binding.textViewFirstNameTitle.text = state.name
            binding.textProtocol.setText(state.protocol)
        }

        return binding.root
    }

}