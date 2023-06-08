package com.example.computerserviceapplast.main.orders.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.computerserviceapplast.R
import com.example.computerserviceapplast.databinding.OrderListItemBinding
import com.example.computerserviceapplast.firebase.Order

class OrderListAdapter : RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    var items: List<Order> = mutableListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var itemClick: (Order) -> Unit = {}
    fun itemClick(listener: (Order) -> Unit) {
        itemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.order_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.order = items[position]
        holder.itemView.setOnClickListener {
            itemClick(items[position])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding = OrderListItemBinding.bind(view)

    }
}