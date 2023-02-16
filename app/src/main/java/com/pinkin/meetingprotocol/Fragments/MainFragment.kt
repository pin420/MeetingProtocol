package com.pinkin.meetingprotocol.Fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.meetingprotocol.Adapter.Adapter
import com.pinkin.meetingprotocol.Adapter.Listener
import com.pinkin.meetingprotocol.GetProtocolsEvent
import com.pinkin.meetingprotocol.GetSearchProtocolsEvent
import com.pinkin.meetingprotocol.R
import com.pinkin.meetingprotocol.ViewModel.MainViewModel
import com.pinkin.meetingprotocol.ViewModel.MainViewModelFactory
import com.pinkin.meetingprotocol.databinding.FragmentMainBinding
import com.pinkin.meetingprotocol.navigator
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.PointerType
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

        GuideView.Builder(requireContext())
            .setTitle("Создайте протокол!")
            .setContentText("Нажмите сюла чтобы создать протокол\n")
            .setTargetView(binding.addProtocol)
            .setGuideListener {
                GuideView.Builder(requireContext())
                    .setTitle("Поиск!")
                    .setContentText("Нажмите сюла чтобы искать протокол\n")
                    .setTargetView(binding.toolbar.findViewById(R.id.app_bar_search))
                    .setGuideListener {
                        Toast.makeText(requireContext(),"Здесь сохранять нажатие",Toast.LENGTH_LONG).show() }
                    .setPointerType(PointerType.arrow)
                    .setDismissType(DismissType.outside)
                    .build()
                    .show() }
            .setPointerType(PointerType.arrow)
            .setDismissType(DismissType.outside)
            .build()
            .show()

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
            if (listProtocols.isEmpty()) {
                binding.meetsRecyclerView.visibility = View.GONE
                binding.emptyRecyclerInfo.visibility = View.VISIBLE
            } else {
                adapterProtocols.protocols = listProtocols
                binding.meetsRecyclerView.visibility = View.VISIBLE
                binding.emptyRecyclerInfo.visibility = View.GONE
            }
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
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.main_toolbar,menu)

        val searchItem: MenuItem = menu.findItem(R.id.app_bar_search)
        val searchView = searchItem.actionView as SearchView

        when(vm.getSearchOption()) {
            0 -> menu.findItem(R.id.searchOption_mans).setChecked(true)
            1 -> menu.findItem(R.id.searchOption_protocol).setChecked(true)
            2 -> menu.findItem(R.id.searchOption_all).setChecked(true)
        }

        searchView.apply {

            setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    setIconifiedByDefault(false)
                    return true
                }
                override fun onQueryTextChange(newTextQuery: String?): Boolean {

                    vm.setSearchQuery(newTextQuery)

                    if (newTextQuery != null) {
                        if(newTextQuery.isEmpty()){
                            vm.send(GetProtocolsEvent())
                        } else {
                            val getSearchProtocolsEvent = GetSearchProtocolsEvent()
                            getSearchProtocolsEvent.setQuery("%$newTextQuery%")
                            vm.send(getSearchProtocolsEvent)
                        }
                    }

                    return true
                }
            })

        }

        searchView.setQuery(vm.getSearchQuery(), true)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val getSearchProtocolsEvent = GetSearchProtocolsEvent()
        getSearchProtocolsEvent.setQuery("%${vm.getSearchQuery()}%")

            when (item.itemId) {
                R.id.searchOption_mans -> {
                    item.isChecked = true
                    vm.setSearchOption(0)
                    vm.send(getSearchProtocolsEvent)
                    return true
                }
                R.id.searchOption_protocol -> {
                    item.isChecked = true
                    vm.setSearchOption(1)
                    vm.send(getSearchProtocolsEvent)
                    return true
                }
                R.id.searchOption_all -> {
                    item.isChecked = true
                    vm.setSearchOption(2)
                    vm.send(getSearchProtocolsEvent)
                    return true
                }
                else -> return false
            }

    }
}




