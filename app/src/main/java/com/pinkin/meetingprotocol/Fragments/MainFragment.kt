package com.pinkin.meetingprotocol.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.meetingprotocol.Adapter.Adapter
import com.pinkin.meetingprotocol.Adapter.Listener
import com.pinkin.meetingprotocol.GetProtocolsEvent
import com.pinkin.meetingprotocol.R
import com.pinkin.meetingprotocol.ViewModel.MainViewModel
import com.pinkin.meetingprotocol.ViewModel.MainViewModelFactory
import com.pinkin.meetingprotocol.databinding.FragmentMainBinding
import com.pinkin.meetingprotocol.navigator
import java.text.SimpleDateFormat


class MainFragment : Fragment() {

    private lateinit var vm: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = ViewModelProvider(requireActivity(), MainViewModelFactory(requireContext()))[MainViewModel::class.java]


        val binding = FragmentMainBinding.inflate(inflater, container, false)

        val adapterProtocols = Adapter(object : Listener {

            override fun choiceItem(protocol: Protocol) {
                Toast.makeText(context,
                    "${protocol.id}" +
                        " ${protocol.name}" +
                        " pressed",
                        Toast.LENGTH_SHORT).show()


                SimpleDateFormat("dd.MM.yyyy").parse(protocol.date)?.let { vm.updateDate(it) }
                val (Hour, Minute) = protocol.time.split(":").map { it.toInt() }
                vm.updateTime(Hour, Minute)

                navigator().showEditProtocol(protocol)
            }
        }

        )
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.meetsRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = adapterProtocols
        }

        vm.send(GetProtocolsEvent())

        vm.protocolsLive.observe(viewLifecycleOwner) { listProtocols ->
            adapterProtocols.protocols = listProtocols
        }

        binding.addProtocol.setOnClickListener {

            navigator().showAddProtocol()
        }

        binding.toolbar.apply {

            inflateMenu(R.menu.main_toolbar)
            setTitle(R.string.app_name)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.searchOption_all,R.id.searchOption_mans,R.id.searchOption_protocol -> {
                        it.setChecked(true)
                        Toast.makeText(context, "${it.title} pressed", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        }

        return binding.root
    }

}