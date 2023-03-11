package com.pinkin.meetingprotocol

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.datasource.Room.Repositories
import com.pinkin.meetingprotocol.Fragments.AboutFragment
import com.pinkin.meetingprotocol.Fragments.AddProtocolFragment
import com.pinkin.meetingprotocol.Fragments.EditProtocolFragment
import com.pinkin.meetingprotocol.Fragments.MainFragment
import com.pinkin.meetingprotocol.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MeetingProtocol)
        super.onCreate(savedInstanceState)

        Repositories.init(this)

        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }


        if (savedInstanceState == null) {

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, MainFragment())
                .commit()
        }
    }

    override fun showAddProtocol() {
        launchFragment(AddProtocolFragment())
    }

    override fun showEditProtocol(protocol: Protocol) {
        launchFragment(EditProtocolFragment.newInstance(protocol))
    }

    override fun showAbout() {
        launchFragment(AboutFragment())
    }

    override fun goToMainFragment() {
        launchMainFragment(MainFragment())
    }

    private fun launchMainFragment(fragment: Fragment) {
        val count = supportFragmentManager.backStackEntryCount
        if (count > 0) {
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.front_enter,
                0,
                0,
                R.anim.front_exit,)
            .addToBackStack(null)
            .add(R.id.fragmentContainer, fragment)
            .commit()
    }
}