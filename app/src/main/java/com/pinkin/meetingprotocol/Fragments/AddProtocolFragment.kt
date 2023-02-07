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
private const val DIALOG_TIME = "DialogTime"
private const val REQUEST_DATETIME = 0


class AddProtocolFragment : Fragment(), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    private lateinit var vm: MainViewModel
    private val formatterDate = SimpleDateFormat("dd.MM.yyyy")
    private val formatterTime = SimpleDateFormat("HH:mm")

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

            setNavigationIcon(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                getActivity()?.onBackPressed();
            }

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
            DatePickerFragment.newInstance(vm.stateLive.value!!.dateTime).apply {
                setTargetFragment(this@AddProtocolFragment, REQUEST_DATETIME)
                show(this@AddProtocolFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        binding.buttonChanceTime.setOnClickListener {
            TimePickerFragment.newInstance(vm.stateLive.value!!.dateTime).apply {
                setTargetFragment(this@AddProtocolFragment, REQUEST_DATETIME)
                show(this@AddProtocolFragment.requireFragmentManager(), DIALOG_TIME)
            }
        }

        vm.stateLive.observe(requireActivity()) {
            binding.textViewDate.text = formatterDate.format(it.dateTime)
            binding.textViewTime.text = formatterTime.format(it.dateTime)
        }

        return binding.root
    }

    override fun onDateSet(date: Date) {
        vm.updateDate(date)
    }

    override fun onTimeSet(hour: Int, minute: Int) {
        vm.updateTime(hour, minute)
    }


}

