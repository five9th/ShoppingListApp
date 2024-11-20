package com.five9th.shoppinglistapp.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.five9th.shoppinglistapp.R
import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class ShopListAdapter : RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameTv: TextView = view.findViewById(R.id.item_name_tv)
        var countTv: TextView = view.findViewById(R.id.item_count_tv)
    }

    private val tag = "ShopListAdapter"

    var shopList: List<ShopListItem> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    private var viewHolderCount = 0 ////

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        Log.d(tag, "onCreateViewHolder, type: $viewType, count: ${++viewHolderCount}") ////

        val layoutId = when (viewType) {
            ITEM_VIEW_TYPE_ENABLED -> R.layout.shop_item_enabled
            ITEM_VIEW_TYPE_DISABLED -> R.layout.shop_item_disabled
            else -> throw Exception("Unknown view type: $viewType")
        }

        val view = LayoutInflater.from(parent.context).inflate(
            layoutId, parent, false
        )

        return ShopItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shopList.count()
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
//        Log.d(tag, "onBindViewHolder, position: $position") ////
        val item = shopList[position]

        holder.nameTv.text = item.name
        holder.countTv.text = item.count.toString()
    }

    override fun getItemViewType(position: Int): Int {
        val item = shopList[position]
        return if (item.isEnabled) ITEM_VIEW_TYPE_ENABLED else ITEM_VIEW_TYPE_DISABLED
    }

    companion object {
        const val ITEM_VIEW_TYPE_ENABLED = 1
        const val ITEM_VIEW_TYPE_DISABLED = 2

        const val MAX_POOL_SIZE = 16
    }
}