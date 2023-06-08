package com.example.computerserviceapp.chat.ui

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.computerserviceapp.R
import com.example.computerserviceapp.chat.models.Message
import com.example.computerserviceapp.databinding.MessageLayoutBinding

class MessageListAdapter (private val ownerId: String) : RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {

    var items: MutableList<Message> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var itemClick: (Message) -> Unit = {}
    fun itemClick(listener: (Message) -> Unit) {
        itemClick = listener
    }


    fun addMessage(message: Message){
        items.add(0, message)
        notifyItemInserted(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = items[position]
        if (message.senderId == ownerId){
            message.senderName = "Вы"
            holder.binding.messageContainer.gravity = Gravity.END
        }
        holder.binding.message = items[position]
        holder.itemView.setOnClickListener {
            itemClick(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = MessageLayoutBinding.bind(view)

    }
}