package com.pinkin.meetingprotocol.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pinkin.meetingprotocol.R
import com.pinkin.meetingprotocol.databinding.AboutAppBinding
import androidx.lifecycle.ViewModelProvider
import com.pinkin.meetingprotocol.DropDatabaseEvent
import com.pinkin.meetingprotocol.SharedPreferences
import com.pinkin.meetingprotocol.ViewModel.MainViewModel
import com.pinkin.meetingprotocol.ViewModel.MainViewModelFactory
import com.pinkin.meetingprotocol.navigator

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

        vm = ViewModelProvider(requireActivity(), MainViewModelFactory())[MainViewModel::class.java]

        val binding = AboutAppBinding.inflate(inflater, container, false)


        binding.toolbar.apply {
            setTitle(R.string.About)

            setNavigationIcon(com.google.android.material.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                activity?.onBackPressed()
            }
        }

        binding.deleteDatabase.setOnClickListener{

            val builder = AlertDialog.Builder(requireContext())

            builder.apply {
                setMessage(R.string.delete_database_dialog)
                setCancelable(true)

                setPositiveButton((R.string.yes)) { dialog, _ ->
                    vm.send(DropDatabaseEvent())
                    dialog.cancel()
                    activity?.onBackPressed() }

                setNegativeButton((R.string.no)) { dialog, _ -> dialog.cancel() }

                create().show()
            }
        }

        binding.deleteLearn.setOnClickListener {

            SharedPreferences.apply {
                setPrefLearnFalse(requireContext(), ACTIVITY)
                setPrefLearnFalse(requireContext(), MAINFRAGMENT)
                setPrefLearnFalse(requireContext(), MAINFRAGMENT2)
                setPrefLearnFalse(requireContext(), ADDFRAGMENT)
                setPrefLearnFalse(requireContext(), EDITFRAGMENT)
            }

            navigator().goToMainFragment()
        }

        return binding.root
    }

}