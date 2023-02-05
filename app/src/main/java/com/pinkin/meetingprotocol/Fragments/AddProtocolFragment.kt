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
import java.text.SimpleDateFormat
import java.util.*

private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0

class AddProtocolFragment : Fragment(), DatePickerFragment.Callbacks {

    private lateinit var vm: MainViewModel
    private val formatter = SimpleDateFormat("dd.MM.yyyy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        vm = ViewModelProvider(this, MainViewModelFactory(requireContext()))[MainViewModel::class.java]

        val binding = AddProtocolBinding.inflate(inflater, container, false)

        binding.toolbar.apply {
            inflateMenu(R.menu.add_protocol_toolbar)
            title = "New protocol"
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.app_bar_done -> {
                        Toast.makeText(context, "Save button pressed", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }

        binding.buttonChanceDate.setOnClickListener {
            DatePickerFragment.newInstance(vm.stateLive.value!!.date).apply {
                setTargetFragment(this@AddProtocolFragment, REQUEST_DATE)
                show(this@AddProtocolFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        vm.stateLive.observe(requireActivity()) {
            binding.textViewDate.text = formatter.format(it.date)
        }

        return binding.root
    }

    override fun onDateSelected(date: Date) {
        vm.updateDate(date)
    }


}

