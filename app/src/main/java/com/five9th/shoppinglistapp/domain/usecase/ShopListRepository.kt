package com.five9th.shoppinglistapp.domain.usecase

import com.five9th.shoppinglistapp.domain.entity.ShopListItem

interface ShopListRepository {
    fun addShopItem(item: ShopListItem)

    fun getShopItem(id: Int): ShopListItem?

    fun editShopItem(item: ShopListItem)

    fun removeShopItem(itemId: Int)

    fun removeShopItem(item: ShopListItem)

    fun getShopList(): List<ShopListItem>
}