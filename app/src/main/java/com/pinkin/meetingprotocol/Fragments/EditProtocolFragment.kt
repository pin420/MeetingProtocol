package com.pinkin.meetingprotocol.Fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.meetingprotocol.DeleteProtocolEvent
import com.pinkin.meetingprotocol.R
import com.pinkin.meetingprotocol.SaveProtocolEvent
import com.pinkin.meetingprotocol.SharedPreferences
import com.pinkin.meetingprotocol.ViewModel.MainViewModel
import com.pinkin.meetingprotocol.ViewModel.MainViewModelFactory
import com.pinkin.meetingprotocol.databinding.FragmentProtocolBinding
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.PointerType
import java.text.SimpleDateFormat
import java.util.*

private const val DIALOG_DATE = "DialogDate"
private const val DIALOG_TIME = "DialogTime"
private const val REQUEST_DATETIME = 0

private const val ARG_PROTOCOL_ID = "arg_protocol_id"
private const val ARG_PROTOCOL_NAME = "arg_protocol_name"
private const val ARG_PROTOCOL = "arg_protocol"

private const val EDITFRAGMENT = "EDITFRAGMENT"

class EditProtocolFragment : Fragment(), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    private lateinit var vm: MainViewModel
    private lateinit var guide2: GuideView
    private lateinit var guide3: GuideView
    private var showNextGuide: Boolean = true
    private lateinit var binding :  FragmentProtocolBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        vm = ViewModelProvider(requireActivity(), MainViewModelFactory())[MainViewModel::class.java]

        binding = FragmentProtocolBinding.inflate(inflater, container, false)
        binding.editTextFirstName.requestFocus()

        binding.editTextFirstName.append(arguments?.getSerializable(ARG_PROTOCOL_NAME) as String)
        binding.textProtocol.append(arguments?.getSerializable(ARG_PROTOCOL) as String)

        binding.toolbar.apply {
            inflateMenu(R.menu.edit_protocol_toolbar)
            setTitle(R.string.edit_protocol)

            setNavigationIcon(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.app_bar_done -> {

                        arguments?.putSerializable(ARG_PROTOCOL_NAME,binding.editTextFirstName.text.toString())
                        arguments?.putSerializable(ARG_PROTOCOL,binding.textProtocol.text.toString())
                        vm.chanceDateOrTime = false

                        val saveProtocolEvent = SaveProtocolEvent()
                        saveProtocolEvent.setId(arguments?.getSerializable(ARG_PROTOCOL_ID) as Int)
                        saveProtocolEvent.setName(arguments?.getSerializable(ARG_PROTOCOL_NAME) as String)
                        saveProtocolEvent.setProtocol(arguments?.getSerializable(ARG_PROTOCOL) as String)
                        vm.send(saveProtocolEvent)

                        activity?.onBackPressed()
                        true
                    }
                    R.id.app_bar_delete -> {

                        val builder = AlertDialog.Builder(requireContext())
                        builder.apply {
                            setMessage(R.string.delete_protocol_dialog)
                            setCancelable(true)

                            setNegativeButton((R.string.no)) { dialog, _ -> dialog.cancel() }

                            setPositiveButton((R.string.yes)) { dialog, _ ->
                                val deleteProtocolEvent = DeleteProtocolEvent()
                                deleteProtocolEvent.setId(arguments?.getSerializable(ARG_PROTOCOL_ID) as Int)
                                vm.send(deleteProtocolEvent)

                                activity?.onBackPressed()
                                dialog.cancel()
                            }

                            create().show()}

                        true
                    }
                    R.id.app_bar_share -> {

                        Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_TEXT,

                                getString(R.string.template_share,binding.textViewDate.text, binding.textViewTime.text,
                                        binding.editTextFirstName.text,
                                        binding.textProtocol.text)
                                )
                        }.also { intent ->
                            startActivity(Intent.createChooser(intent, getString(R.string.share)))
                        }

                        true
                    }
                    else -> false
                }
            }
        }

        guide2 =
        GuideView.Builder(requireContext())
        .setTitle(getString(R.string.guide_editProtocol_2_title))
        .setContentText(getString(R.string.guide_editProtocol_2_contentTitle))
        .setTargetView(binding.toolbar.findViewById(R.id.app_bar_share))
        .setGuideListener {
            SharedPreferences.setPrefLearnTrue(requireContext(), EDITFRAGMENT)
            Snackbar.make(binding.root,getString(R.string.guide_editProtocol_2_snackbar), Snackbar.LENGTH_LONG).show() }
        .setPointerType(PointerType.arrow)
        .setDismissType(DismissType.outside)
        .build()

        guide3 =
        GuideView.Builder(requireContext())
        .setTitle(getString(R.string.guide_editProtocol_1_title))
        .setContentText(getString(R.string.guide_editProtocol_1_contentTitle))
        .setTargetView(binding.toolbar.findViewById(R.id.app_bar_delete))
        .setGuideListener {
            if(showNextGuide){ guide2.show()}
        }
        .setPointerType(PointerType.arrow)
        .setDismissType(DismissType.outside)
        .build()

        if (!SharedPreferences.getPrefLearn(requireContext(), EDITFRAGMENT)) {
            guide3.show()
        }


        binding.buttonChanceDate.setOnClickListener {
            DatePickerFragment.newInstance(vm.stateLive.value!!.dateTime).apply {
                setTargetFragment(this@EditProtocolFragment, REQUEST_DATETIME)
                show(this@EditProtocolFragment.requireFragmentManager(), DIALOG_DATE)
            }
                vm.chanceDateOrTime = true
        }

        binding.buttonChanceTime.setOnClickListener {
            TimePickerFragment.newInstance(vm.stateLive.value!!.dateTime).apply {
                setTargetFragment(this@EditProtocolFragment, REQUEST_DATETIME)
                show(this@EditProtocolFragment.requireFragmentManager(), DIALOG_TIME)
            }
                vm.chanceDateOrTime = true
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

    fun checkChanges() : Boolean{
        return (
        !vm.chanceDateOrTime &&
        binding.editTextFirstName.text.toString() == arguments?.getSerializable(ARG_PROTOCOL_NAME) as String &&
        binding.textProtocol.text.toString() == arguments?.getSerializable(ARG_PROTOCOL) as String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(!checkChanges()){
                    vm.chanceDateOrTime = false
                    val builder = AlertDialog.Builder(requireContext())
                    builder.apply {
                        setMessage(R.string.save_changes_dialog)
                        setCancelable(true)

                        setNeutralButton((R.string.cancel)) { dialog, _ -> dialog.cancel() }

                        setNegativeButton((R.string.no)) { dialog, _ ->
                            dialog.cancel()
                            isEnabled = false
                            activity?.onBackPressed()
                        }

                        setPositiveButton((R.string.yes)) { dialog, _ ->
                            val saveProtocolEvent = SaveProtocolEvent()
                            saveProtocolEvent.setId(arguments?.getSerializable(ARG_PROTOCOL_ID) as Int)
                            saveProtocolEvent.setName(binding.editTextFirstName.text.toString())
                            saveProtocolEvent.setProtocol(binding.textProtocol.text.toString())
                            vm.send(saveProtocolEvent)

                            dialog.cancel()
                            isEnabled = false
                            activity?.onBackPressed()
                        }

                        create().show()}
                }else{
                    isEnabled = false
                    activity?.onBackPressed()
                }
            }
        })}

    override fun onDestroy() {
        showNextGuide = false

        if(guide3.isShowing){
            guide3.dismiss()}
        if(guide2.isShowing){
            guide2.dismiss()}

        super.onDestroy()
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

