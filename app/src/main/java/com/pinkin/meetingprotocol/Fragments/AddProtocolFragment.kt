package com.pinkin.meetingprotocol.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pinkin.meetingprotocol.R
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


        binding.toolbar.inflateMenu(R.menu.add_protocol_toolbar)
        binding.toolbar.setTitle("New protocol")
        binding.toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.app_bar_done) {
                Toast.makeText(context, "Save button pressed", Toast.LENGTH_SHORT).show()
                true
            }
            else false
        }



        return binding.root
    }


}