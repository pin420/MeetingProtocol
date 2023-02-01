package com.pinkin.meetingprotocol

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pinkin.meetingprotocol.Fragments.MainFragment
import com.pinkin.meetingprotocol.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }


        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, MainFragment())
                .commit()
        }

    }
}