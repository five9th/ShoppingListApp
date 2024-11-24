package com.five9th.shoppinglistapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.five9th.shoppinglistapp.R
import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class ShopListAdapter :
    ListAdapter<ShopListItem, ShopListAdapter.ShopItemViewHolder>(ShopItemDiffCallback()) {

    class ShopItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var nameTv: TextView = view.findViewById(R.id.item_name_tv)
        var countTv: TextView = view.findViewById(R.id.item_count_tv)
    }


    var onLongClickListener: ((ShopListItem) -> Unit)? = null
    var onClickListener: ((ShopListItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
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

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val item = getItem(position)

        holder.nameTv.text = item.name
        holder.countTv.text = item.count.toString()

        holder.itemView.setOnLongClickListener {
            onLongClickListener?.invoke(item)
            true
        }

        holder.itemView.setOnClickListener {
            onClickListener?.invoke(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return if (item.isEnabled) ITEM_VIEW_TYPE_ENABLED else ITEM_VIEW_TYPE_DISABLED
    }

    companion object {
        const val ITEM_VIEW_TYPE_ENABLED = 1
        const val ITEM_VIEW_TYPE_DISABLED = 2

        const val MAX_POOL_SIZE = 16
    }
}