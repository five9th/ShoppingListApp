package com.five9th.shoppinglistapp.domain.usecase

import com.five9th.shoppinglistapp.domain.entity.ShopListItem

interface ShoplistRepository {
    fun addShopItem(item: ShopListItem)

    fun getShopItem(id: Int): ShopListItem

    fun editShopItem(item: ShopListItem)

    fun removeShopItem(itemId: Int)

    fun getShopList(): List<ShopListItem>
}