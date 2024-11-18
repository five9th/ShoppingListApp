package com.five9th.shoppinglistapp.data

import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import com.five9th.shoppinglistapp.domain.usecase.ShopListRepository

object ShopListRepositoryImpl : ShopListRepository {
    private val shopList = mutableListOf<ShopListItem>()

    private var lastId = 0

    init {
        addShopItem(ShopListItem("one", 1, true))
        addShopItem(ShopListItem("two", 2, true))
        addShopItem(ShopListItem("three", 3, true))
    }

    override fun addShopItem(item: ShopListItem) {
        if (item.id == ShopListItem.UNREGISTERED_ID) {
            item.id = ++lastId
        }

        shopList.add(item)
    }

    override fun getShopItem(id: Int): ShopListItem? {
        return shopList.find { item -> item.id == id }
    }

    override fun editShopItem(item: ShopListItem) {
        removeShopItem(item.id)
        addShopItem(item)
    }

    override fun removeShopItem(itemId: Int) {
        var index = -1

        for (i in shopList.indices) {
            val item = shopList[i]

            if (item.id == itemId) {
                index = i
                break
            }
        }

        if (index in shopList.indices) {
            shopList.removeAt(index)
        }
    }

    override fun removeShopItem(item: ShopListItem) {
        removeShopItem(item.id)
    }

    override fun getShopList(): List<ShopListItem> {
        return shopList
    }
}