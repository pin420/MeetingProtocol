package com.pinkin.meetingprotocol.Fragments

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

                SimpleDateFormat("dd.MM.yyyy").parse(protocol.date)?.let {date ->
                    vm.updateDate(date) }

                val (Hour, Minute) = protocol.time.split(":").map { it.toInt() }
                vm.updateTime(Hour, Minute)

                navigator().showEditProtocol(protocol)
            }
        })

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

            vm.updateDateTime()

            navigator().showAddProtocol()
        }


        binding.toolbar.setTitle(R.string.app_name)


        (requireContext() as AppCompatActivity?)?.setSupportActionBar(binding.toolbar)

        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_toolbar,menu)

        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Toast.makeText(requireContext(),"sub",Toast.LENGTH_SHORT).show()
                return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
                Toast.makeText(requireContext(),"update",Toast.LENGTH_SHORT).show()
                return true
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.searchOption_all -> {
                    item.isChecked = true
                    vm.setSearchOption(0)
                    return true
                }
                R.id.searchOption_mans -> {
                    item.isChecked = true
                    vm.setSearchOption(1)
                    return true
                }
                R.id.searchOption_protocol -> {
                    item.isChecked = true
                    vm.setSearchOption(2)
                    return true
                }
                else -> return false
            }

    }
}




