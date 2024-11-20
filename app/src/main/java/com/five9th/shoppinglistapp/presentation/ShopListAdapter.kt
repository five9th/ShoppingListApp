package com.five9th.shoppinglistapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.five9th.shoppinglistapp.R
import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    class ShopItemViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var tvName: TextView = view.findViewById(R.id.item_name_tv)
        var tvCount: TextView = view.findViewById(R.id.item_count_tv)
    }

    var shopList : List<ShopListItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.shop_item_enabled,
            parent,
            false
        )

        return ShopItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shopList.count()
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val item = shopList[position]

        holder.tvName.text = item.name
        holder.tvCount.text = item.count.toString()
    }


}