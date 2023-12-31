package com.pinkin.meetingprotocol.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pinkin.businesslogic.Model.Protocol
import com.pinkin.meetingprotocol.databinding.ItemMeetBinding

class DiffCallback(
    private val oldList: List<Protocol>,
    private val newList: List<Protocol>
):DiffUtil.Callback(){

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser.id == newUser.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldUser = oldList[oldItemPosition]
        val newUser = newList[newItemPosition]
        return oldUser == newUser
    }

}

class Adapter(
    private val listener: Listener
) : RecyclerView.Adapter<Adapter.ViewHolder>(), View.OnClickListener {

    var protocols: List<Protocol> = emptyList()
        set(newValue){
            val diffCallback = DiffCallback(field, newValue)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = newValue
            diffResult.dispatchUpdatesTo(this)
        }


    override fun getItemCount(): Int = protocols.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMeetBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val protocol = protocols[position]

        holder.binding.root.tag = protocol

        holder.binding.apply {
            meetTitle.text = protocol.name.take(150)
            meetDate.text = protocol.date
            meetTime.text = protocol.time
            meetProtocol.text = protocol.protocol.take(150)
        }
    }

    override fun onClick(v: View) {
        val protocol = v.tag as Protocol
        listener.choiceItem(protocol)
    }


    class ViewHolder(val binding: ItemMeetBinding) : RecyclerView.ViewHolder(binding.root)
}

interface Listener {

    fun choiceItem(protocol: Protocol)
}