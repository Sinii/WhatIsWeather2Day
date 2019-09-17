package com.example.base.adapters

import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<VH : RecyclerView.ViewHolder, I>(
    var items: ArrayList<I>
) : RecyclerView.Adapter<VH>() {

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItems(list: List<I>) {
        items.addAll(list)
    }

    fun clearItems() {
        items.clear()
    }

    fun getItem(position: Int) = items[position]

    fun hasItems() = items.size > 0

}