package com.pinkin.meetingprotocol.Fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.meetingprotocol.*
import com.pinkin.meetingprotocol.Adapter.Adapter
import com.pinkin.meetingprotocol.Adapter.Listener
import com.pinkin.meetingprotocol.ViewModel.MainViewModel
import com.pinkin.meetingprotocol.ViewModel.MainViewModelFactory
import com.pinkin.meetingprotocol.databinding.FragmentMainBinding
import smartdevelop.ir.eram.showcaseviewlib.GuideView
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType
import smartdevelop.ir.eram.showcaseviewlib.config.PointerType
import java.text.SimpleDateFormat

private const val ACTIVITY = "ACTIVITY"
private const val MAINFRAGMENT = "MAINFRAGMENT"
private const val MAINFRAGMENT2 = "MAINFRAGMENT2"
private const val ADDFRAGMENT = "ADDFRAGMENT"
private const val EDITFRAGMENT = "EDITFRAGMENT"

class MainFragment : Fragment() {

    private lateinit var vm: MainViewModel
    private lateinit var guide1: GuideView
    private lateinit var searchView: SearchView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        vm = ViewModelProvider(requireActivity(), MainViewModelFactory(requireContext()))[MainViewModel::class.java]

        val binding = FragmentMainBinding.inflate(inflater, container, false)


        guide1 =
            GuideView.Builder(requireActivity())
                .setTitle("Ваша встреча сохранена")
                .setContentText("Нажмите на карточку, если нужно отредактировать информацию")
                .setTargetView(binding.meetsRecyclerView)
                .setGuideListener {
                    SharedPreferences.setPrefLearnTrue(requireContext(), MAINFRAGMENT2)
                    Snackbar.make(binding.root,"Откройте свой первый протокол",Snackbar.LENGTH_INDEFINITE).show()
                }
                .setPointerType(PointerType.arrow)
                .setDismissType(DismissType.outside)
                .build()

        if (savedInstanceState == null) {
            if (!SharedPreferences.getPrefLearn(requireContext(), ACTIVITY)) {
                val intent = Intent(requireContext(), MyAppIntro::class.java)
                startActivity(intent)
            }
        }


        if (!SharedPreferences.getPrefLearn(requireContext(), MAINFRAGMENT)) {


            GuideView.Builder(requireContext())
                .setTitle("Добавьте протокол")
                .setContentText("Нажмите на кнопку, чтобы создать новую сводку")
                .setTargetView(binding.addProtocol)
                .setGuideListener {
                    GuideView.Builder(requireContext())
                        .setTitle("Найдите сводку")
                        .setContentText("Нажмите на значок, чтобы найти нужную сводку.")
                        .setTargetView(binding.toolbar.findViewById(R.id.app_bar_search))
                        .setGuideListener {
                            SharedPreferences.setPrefLearnTrue(requireContext(), MAINFRAGMENT)
                            Snackbar.make(binding.root,"Создайте свой первый протокол",Snackbar.LENGTH_INDEFINITE).show() }
                        .setPointerType(PointerType.arrow)
                        .setDismissType(DismissType.outside)
                        .build()
                        .show() }
                .setPointerType(PointerType.arrow)
                .setDismissType(DismissType.outside)
                .build()
                .show()
        }

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
        linearLayoutManager.recycleChildrenOnDetach = true
        binding.meetsRecyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = adapterProtocols
            recycledViewPool.setMaxRecycledViews(0,15)

            if(!vm.getRecyclerViewPoolInit()){
                vm.setRecyclerViewPoolInit()
                vm.setRecyclerPool(recycledViewPool)
            }else{
                setRecycledViewPool(vm.getRecyclerPool())
            }
        }



        vm.protocolsLive.observe(viewLifecycleOwner) { listProtocols ->
            val enterAnim: Animation = AnimationUtils.loadAnimation(requireContext(), com.google.android.material.R.anim.abc_fade_in)
            if (listProtocols.isEmpty()) {

                binding.emptyRecyclerTextview.text =
                    when (vm.lastEventForLoad) {
                        is GetProtocolsEvent -> getString(R.string.emptyDataBase)
                        is GetSearchProtocolsEvent -> getString(R.string.emptySearchResults)
                        else -> {getString(R.string.emptySearchResults)}
                    }

                binding.meetsRecyclerView.visibility = View.GONE

                if (binding.emptyRecyclerInfo.visibility != View.VISIBLE) {
                        binding.emptyRecyclerInfo.startAnimation(enterAnim)
                        binding.emptyRecyclerInfo.visibility = View.VISIBLE}
            } else {
                adapterProtocols.protocols = listProtocols

                if (binding.meetsRecyclerView.visibility != View.VISIBLE) {
                    binding.meetsRecyclerView.startAnimation(enterAnim)
                    binding.meetsRecyclerView.visibility = View.VISIBLE}

                binding.emptyRecyclerInfo.visibility = View.GONE

                if (SharedPreferences.getPrefLearn(requireContext(), ADDFRAGMENT)) {
                    if (!SharedPreferences.getPrefLearn(requireContext(), EDITFRAGMENT)) {
                        if (!SharedPreferences.getPrefLearn(requireContext(), MAINFRAGMENT2)) {
                            if(!guide1.isShowing) {
                                binding.meetsRecyclerView.layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                                adapterProtocols.protocols = listOf(listProtocols[0])
                                guide1.show()
                            }
                        }
                    }
                }
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
        searchView = searchItem.actionView as SearchView

        when(vm.getSearchOption()) {
            0 -> {
                menu.findItem(R.id.searchOption_mans).isChecked = true
                searchView.queryHint = resources.getString(R.string.search_option_mans);
            }
            1 -> {
                menu.findItem(R.id.searchOption_protocol).isChecked = true
                searchView.queryHint = resources.getString(R.string.search_option_protocol);
            }
            2 -> {
                menu.findItem(R.id.searchOption_all).isChecked = true
                searchView.queryHint = resources.getString(R.string.search_option_all);
            }
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
                    searchView.queryHint = getResources().getString(R.string.search_option_mans);
                    return true
                }
                R.id.searchOption_protocol -> {
                    item.isChecked = true
                    vm.setSearchOption(1)
                    vm.send(getSearchProtocolsEvent)
                    searchView.queryHint = getResources().getString(R.string.search_option_protocol);
                    return true
                }
                R.id.searchOption_all -> {
                    item.isChecked = true
                    vm.setSearchOption(2)
                    vm.send(getSearchProtocolsEvent)
                    searchView.queryHint = getResources().getString(R.string.search_option_all);
                    return true
                }
                R.id.about_app -> {
                    navigator().showAbout()
                    return true
                }
                else -> return false
            }

    }

    override fun onDestroy() {
        vm.getRecyclerPool().clear()
        super.onDestroy()
    }
}




