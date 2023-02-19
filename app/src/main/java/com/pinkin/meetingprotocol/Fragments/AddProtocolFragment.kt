package com.pinkin.meetingprotocol.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

private const val ADDFRAGMENT = "ADDFRAGMENT"

class AddProtocolFragment : Fragment(), DatePickerFragment.Callbacks, TimePickerFragment.Callbacks {

    private lateinit var vm: MainViewModel
    private lateinit var guide1: GuideView
    private lateinit var guide2: GuideView
    private lateinit var guide3: GuideView
    private lateinit var guide4: GuideView
    private var showNextGuide: Boolean = true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        vm = ViewModelProvider(requireActivity(), MainViewModelFactory(requireContext()))[MainViewModel::class.java]

        val binding = FragmentProtocolBinding.inflate(inflater, container, false)


        binding.toolbar.apply {
            inflateMenu(R.menu.add_protocol_toolbar)
            setTitle(R.string.new_protocol)

            setNavigationIcon(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                getActivity()?.onBackPressed();
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.app_bar_done -> {

                        val saveProtocolEvent = SaveProtocolEvent()
                        saveProtocolEvent.setName(binding.editTextFirstName.text.toString())
                        saveProtocolEvent.setProtocol(binding.textProtocol.text.toString())
                        vm.send(saveProtocolEvent)

                        getActivity()?.onBackPressed();
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


            guide1 =
                GuideView.Builder(requireActivity())
                    .setTitle("Сохраните протокол!")
                    .setContentText("Нажмите сюда чтобы сохранить\n")
                    .setTargetView(binding.toolbar.findViewById(R.id.app_bar_done))
                    .setGuideListener {
                        SharedPreferences.setPrefLearn(requireContext(), ADDFRAGMENT)
                        Toast.makeText(
                            requireContext(),
                            "Здесь сохранять нажатие",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .setPointerType(PointerType.arrow)
                    .setDismissType(DismissType.outside)
                    .build()

            guide2 =
                GuideView.Builder(requireActivity())
                    .setTitle("Запишите протокол!")
                    .setContentText("Нажмите сюда чтобы написать протокол\n")
                    .setTargetView(binding.textProtocol)
                    .setGuideListener {
                        if(showNextGuide){
                            guide1.show()}
                    }
                    .setPointerType(PointerType.arrow)
                    .setDismissType(DismissType.outside)
                    .build()

            guide3 =
                GuideView.Builder(requireActivity())
                    .setTitle("Изменяйте время!")
                    .setContentText("Нажмите сюда чтобы время и дату\n")
                    .setTargetView(binding.buttonChanceDate)
                    .setGuideListener {
                        if(showNextGuide){
                            guide2.show()}
                    }
                    .setPointerType(PointerType.arrow)
                    .setDismissType(DismissType.outside)
                    .build()

            guide4 =
            GuideView.Builder(requireActivity())
                .setTitle("Добавьте участников встречи!")
                .setContentText("Нажмите сюда чтобы добавить участников\n")
                .setTargetView(binding.editTextFirstName)
                .setGuideListener {
                    if(showNextGuide){
                    guide3.show()}
                   }
                .setPointerType(PointerType.arrow)
                .setDismissType(DismissType.outside)
                .build()

        if (!SharedPreferences.getPrefLearn(requireContext(), ADDFRAGMENT)) {
            guide4.show()
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

        if(guide4.isShowing){
        guide4.dismiss()}
        if(guide3.isShowing){
            guide3.dismiss()}
        if(guide2.isShowing){
            guide2.dismiss()}
        if(guide1.isShowing){
            guide1.dismiss()}


        super.onDestroy()
    }
}

