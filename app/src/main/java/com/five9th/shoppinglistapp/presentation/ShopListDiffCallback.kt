package com.five9th.shoppinglistapp.presentation

import androidx.recyclerview.widget.DiffUtil
import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class ShopListDiffCallback(
    private val oldList: List<ShopListItem>,
    private val newList: List<ShopListItem>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        // Items are the same is their ids are the same
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}