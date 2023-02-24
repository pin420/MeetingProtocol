package com.pinkin.meetingprotocol.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pinkin.meetingprotocol.R
import com.pinkin.meetingprotocol.databinding.AboutAppBinding
import android.content.DialogInterface.OnClickListener
import androidx.lifecycle.ViewModelProvider
import com.pinkin.meetingprotocol.DropDatabaseEvent
import com.pinkin.meetingprotocol.SharedPreferences
import com.pinkin.meetingprotocol.ViewModel.MainViewModel
import com.pinkin.meetingprotocol.ViewModel.MainViewModelFactory

private const val ACTIVITY = "ACTIVITY"
private const val MAINFRAGMENT = "MAINFRAGMENT"
private const val MAINFRAGMENT2 = "MAINFRAGMENT2"
private const val ADDFRAGMENT = "ADDFRAGMENT"
private const val EDITFRAGMENT = "EDITFRAGMENT"

class AboutFragment : Fragment() {


    private lateinit var vm: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        vm = ViewModelProvider(requireActivity(), MainViewModelFactory(requireContext()))[MainViewModel::class.java]

        val binding = AboutAppBinding.inflate(inflater, container, false)


        binding.toolbar.apply {
            setTitle(R.string.About)

            setNavigationIcon(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                getActivity()?.onBackPressed();
            }
        }

        binding.deleteDatabase.setOnClickListener{

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage(R.string.delete_database_dialog)
            builder.setCancelable(true)

            builder.setPositiveButton(
                (R.string.yes),
                OnClickListener { dialog, id ->

                    vm.send(DropDatabaseEvent())

                    dialog.cancel()
                    getActivity()?.onBackPressed();
                })

            builder.setNegativeButton(
                (R.string.no),
                OnClickListener { dialog, id -> dialog.cancel() })

            builder.create().show()


        }

        binding.deleteLearn.setOnClickListener {

            SharedPreferences.setPrefLearnFalse(requireContext(), ACTIVITY)
            SharedPreferences.setPrefLearnFalse(requireContext(), MAINFRAGMENT)
            SharedPreferences.setPrefLearnFalse(requireContext(), MAINFRAGMENT2)
            SharedPreferences.setPrefLearnFalse(requireContext(), ADDFRAGMENT)
            SharedPreferences.setPrefLearnFalse(requireContext(), EDITFRAGMENT)

            getActivity()?.onBackPressed();
        }







        return binding.root
    }



}