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

class EditProtocolFragment() : Fragment(), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    private lateinit var vm: MainViewModel
    private lateinit var guide1: GuideView
    private lateinit var guide2: GuideView
    private lateinit var guide3: GuideView
    private var showNextGuide: Boolean = true


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
            setTitle(R.string.edit_protocol)

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
                        builder.setMessage(R.string.delete_dialog)
                        builder.setCancelable(true)

                        builder.setPositiveButton(
                            (R.string.yes),
                            OnClickListener { dialog, id ->
                                val deleteProtocolEvent = DeleteProtocolEvent()
                                deleteProtocolEvent.setId(arguments?.getSerializable(ARG_PROTOCOL_ID) as Int)
                                vm.send(deleteProtocolEvent)

                                getActivity()?.onBackPressed();
                                dialog.cancel() })

                        builder.setNegativeButton(
                            (R.string.no),
                            OnClickListener { dialog, id -> dialog.cancel() })

                        builder.create().show()

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

        guide1 =
        GuideView.Builder(requireContext())
        .setTitle("Сохранение изменений!")
        .setContentText("Ваши изменения будут сохранены по нажатию\n")
        .setTargetView(binding.toolbar.findViewById(R.id.app_bar_done))
        .setGuideListener {
            SharedPreferences.setPrefLearnTrue(requireContext(), EDITFRAGMENT)
            Snackbar.make(binding.root,"ОБУЧЕНИЕ ЗАВЕРШЕНО! УДАЧИ", Snackbar.LENGTH_LONG).show() }
        .setPointerType(PointerType.arrow)
        .setDismissType(DismissType.outside)
        .build()

        guide2 =
        GuideView.Builder(requireContext())
        .setTitle("Поделиться протоколом!")
        .setContentText("Здесь можно отправить протокол через другие приложения\n")
        .setTargetView(binding.toolbar.findViewById(R.id.app_bar_share))
        .setGuideListener {
            if(showNextGuide){ guide1.show()}
        }
        .setPointerType(PointerType.arrow)
        .setDismissType(DismissType.outside)
        .build()

        guide3 =
        GuideView.Builder(requireContext())
        .setTitle("Удаление протокола!")
        .setContentText("Ваш протокол будет удалён по нажатию\n")
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

    override fun onDestroy() {
        showNextGuide = false

        if(guide3.isShowing){
            guide3.dismiss()}
        if(guide2.isShowing){
            guide2.dismiss()}
        if(guide1.isShowing){
            guide1.dismiss()}

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

