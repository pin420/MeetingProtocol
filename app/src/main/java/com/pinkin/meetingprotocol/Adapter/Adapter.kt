package com.pinkin.meetingprotocol.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.meetingprotocol.databinding.ItemMeetBinding

class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

    var protocols: List<Protocol> = emptyList()
        @SuppressLint("NotifyDataSetChanged")
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }


    override fun getItemCount(): Int = protocols.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMeetBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val protocol = protocols[position]

        holder.binding.apply {
            meetTitle.text = protocol.name
            meetDate.text = protocol.date
            meetTime.text = protocol.time
            meetProtocol.text = protocol.protocol
        }
    }


    class ViewHolder(val binding: ItemMeetBinding) : RecyclerView.ViewHolder(binding.root)
}