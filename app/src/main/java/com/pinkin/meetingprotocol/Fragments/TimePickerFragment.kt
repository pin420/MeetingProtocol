package com.pinkin.meetingprotocol.Fragments

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*
import android.text.format.DateFormat
import android.widget.TimePicker

private const val ARG_TIME = "time"

class TimePickerFragment : DialogFragment() {

    interface Callbacks {
        fun onTimeSet(hour: Int, minute: Int)
    }

    private val timeListener = TimePickerDialog.OnTimeSetListener {
            _: TimePicker, hour: Int, minute: Int ->

        targetFragment?.let { fragment ->
            (fragment as Callbacks).onTimeSet(hour, minute)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar: Calendar = Calendar.getInstance()
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)

        return TimePickerDialog(
            requireContext(),
            timeListener,
            hour,
            minute,
            DateFormat.is24HourFormat(requireContext())
        )
    }

    companion object {
        fun newInstance(time: Date): TimePickerFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TIME, time)
            }

            return TimePickerFragment().apply {
                arguments = args
            }
        }
    }
}