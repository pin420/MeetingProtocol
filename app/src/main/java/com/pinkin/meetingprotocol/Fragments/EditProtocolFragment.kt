package com.pinkin.meetingprotocol.Fragments

import android.app.AlertDialog
import android.content.DialogInterface.OnClickListener
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.meetingprotocol.DeleteProtocolEvent
import com.pinkin.meetingprotocol.R
import com.pinkin.meetingprotocol.SaveProtocolEvent
import com.pinkin.meetingprotocol.ViewModel.MainViewModel
import com.pinkin.meetingprotocol.ViewModel.MainViewModelFactory
import com.pinkin.meetingprotocol.databinding.FragmentProtocolBinding
import java.text.SimpleDateFormat
import java.util.*

private const val DIALOG_DATE = "DialogDate"
private const val DIALOG_TIME = "DialogTime"
private const val REQUEST_DATETIME = 0

private const val ARG_PROTOCOL_ID = "arg_protocol_id"
private const val ARG_PROTOCOL_NAME = "arg_protocol_name"
private const val ARG_PROTOCOL = "arg_protocol"

class EditProtocolFragment() : Fragment(), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    private lateinit var vm: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        vm = ViewModelProvider(requireActivity(), MainViewModelFactory(requireContext()))[MainViewModel::class.java]

        val binding = FragmentProtocolBinding.inflate(inflater, container, false)


        binding.editTextFirstName.append(arguments?.getSerializable(ARG_PROTOCOL_NAME) as String)
        binding.textProtocol.append(arguments?.getSerializable(ARG_PROTOCOL) as String)

        binding.toolbar.apply {
            inflateMenu(R.menu.edit_protocol_toolbar)
            title = "Edit protocol"

            setNavigationIcon(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                getActivity()?.onBackPressed();
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.app_bar_done -> {

                        val saveProtocolEvent = SaveProtocolEvent()
                        saveProtocolEvent.setId(arguments?.getSerializable(ARG_PROTOCOL_ID) as Int)
                        saveProtocolEvent.setName(binding.editTextFirstName.text.toString())
                        saveProtocolEvent.setProtocol(binding.textProtocol.text.toString())
                        vm.send(saveProtocolEvent)

                        getActivity()?.onBackPressed();
                        true
                    }
                    R.id.app_bar_delete -> {

                        val builder = AlertDialog.Builder(requireContext())
                        builder.setMessage("Вы уверенны что хотите удалить протокол?")
                        builder.setCancelable(true)

                        builder.setPositiveButton(
                            "Да",
                            OnClickListener { dialog, id ->
                                val deleteProtocolEvent = DeleteProtocolEvent()
                                deleteProtocolEvent.setId(arguments?.getSerializable(ARG_PROTOCOL_ID) as Int)
                                vm.send(deleteProtocolEvent)

                                getActivity()?.onBackPressed();
                                dialog.cancel() })

                        builder.setNegativeButton(
                            "Нет",
                            OnClickListener { dialog, id -> dialog.cancel() })

                        builder.create().show()

                        true
                    }
                    R.id.app_bar_share -> {

                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT,
                                        "Дата:${binding.textViewDate.text} ${binding.textViewTime.text}\n" +
                                "Участники:\n${binding.editTextFirstName.text}\n" +
                                    "Протокол:\n${binding.textProtocol.text}\n\n" +
                                                "Сделано с помощью приложения Meeting protocol"
                                )
                        }.also { intent ->
                            startActivity(Intent.createChooser(intent, "Share"))
                        }

                        true
                    }
                    else -> false
                }
            }
        }



        binding.buttonChanceDate.setOnClickListener {
            DatePickerFragment.newInstance(vm.stateLive.value!!.dateTime).apply {
                setTargetFragment(this@EditProtocolFragment, REQUEST_DATETIME)
                show(this@EditProtocolFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        binding.buttonChanceTime.setOnClickListener {
            TimePickerFragment.newInstance(vm.stateLive.value!!.dateTime).apply {
                setTargetFragment(this@EditProtocolFragment, REQUEST_DATETIME)
                show(this@EditProtocolFragment.requireFragmentManager(), DIALOG_TIME)
            }
        }

        vm.stateLive.observe(viewLifecycleOwner) {
            binding.textViewDate.text = SimpleDateFormat("dd.MM.yyyy").format(it.dateTime)
            binding.textViewTime.text = SimpleDateFormat("HH:mm").format(it.dateTime)
        }

        return binding.root
    }

    override fun onDateSet(date: Date) {
        vm.updateDate(date)
    }

    override fun onTimeSet(hour: Int, minute: Int) {
        vm.updateTime(hour, minute)
    }

    companion object {
        fun newInstance(protocol: Protocol): EditProtocolFragment {
            val args = Bundle().apply {
                putSerializable(ARG_PROTOCOL_ID, protocol.id)
                putSerializable(ARG_PROTOCOL_NAME, protocol.name)
                putSerializable(ARG_PROTOCOL, protocol.protocol)
            }
            return EditProtocolFragment().apply {
                arguments = args
            }
        }

    }

}

