package com.pinkin.meetingprotocol

import androidx.fragment.app.Fragment

interface Navigator {

    fun showAddProtocol()

}

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

