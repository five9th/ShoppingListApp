package com.five9th.shoppinglistapp.domain.usecase

import androidx.lifecycle.LiveData
import com.five9th.shoppinglistapp.domain.entity.ShopListItem

interface ShopListRepository {
    suspend fun addShopItem(item: ShopListItem)

    suspend fun getShopItem(id: Int): ShopListItem?

    suspend fun editShopItem(item: ShopListItem)

    suspend fun removeShopItem(itemId: Int)

    suspend fun removeShopItem(item: ShopListItem)

    fun getShopListLD(): LiveData<List<ShopListItem>>
}