package com.five9th.shoppinglistapp.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.five9th.shoppinglistapp.R
import com.five9th.shoppinglistapp.databinding.ShopItemDisabledBinding
import com.five9th.shoppinglistapp.databinding.ShopItemEnabledBinding
import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import java.util.Locale

class ShopListAdapter :
    ListAdapter<ShopListItem, ShopListAdapter.ShopItemViewHolder>(ShopItemDiffCallback()) {

    class ShopItemViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root)


    var onLongClickListener: ((ShopListItem) -> Unit)? = null
    var onClickListener: ((ShopListItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {
        val layoutId = when (viewType) {
            ITEM_VIEW_TYPE_ENABLED -> R.layout.shop_item_enabled
            ITEM_VIEW_TYPE_DISABLED -> R.layout.shop_item_disabled
            else -> throw Exception("Unknown view type: $viewType")
        }

        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            layoutId,
            parent,
            false
        )

        return ShopItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val item = getItem(position)

        holder.itemView.setOnLongClickListener {
            onLongClickListener?.invoke(item)
            true
        }

        holder.itemView.setOnClickListener {
            onClickListener?.invoke(item)
        }

        when (val binding = holder.binding) {
            is ShopItemEnabledBinding -> {
                binding.itemNameTv.text = item.name
                binding.itemCountTv.text = intToText(item.count)
            }
            is ShopItemDisabledBinding -> {
                binding.itemNameTv.text = item.name
                binding.itemCountTv.text = intToText(item.count)
            }
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

        private fun intToText(number: Int): String {
            return String.format(Locale.getDefault(), "%d", number)
        }
    }
}