package com.five9th.shoppinglistapp.presentation

import androidx.recyclerview.widget.DiffUtil
import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class ShopItemDiffCallback : DiffUtil.ItemCallback<ShopListItem>() {
    override fun areItemsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ShopListItem, newItem: ShopListItem): Boolean {
        return oldItem == newItem
    }
}