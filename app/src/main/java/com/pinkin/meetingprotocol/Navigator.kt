package com.pinkin.meetingprotocol

import androidx.fragment.app.Fragment
import com.pinkin.businesslogic.Model.Protocol

interface Navigator {

    fun showAddProtocol()

    fun showEditProtocol(protocol: Protocol)

    fun showAbout()

}

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

