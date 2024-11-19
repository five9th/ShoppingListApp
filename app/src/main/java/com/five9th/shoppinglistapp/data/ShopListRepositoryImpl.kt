package com.five9th.shoppinglistapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import com.five9th.shoppinglistapp.domain.usecase.ShopListRepository


/** Simple repository implementation. Stores data in local variable.
 * Also stores and manages LiveData. */
object ShopListRepositoryImpl : ShopListRepository {
    private val shopList =
        sortedSetOf<ShopListItem>(
            { i1, i2 -> i1.id.compareTo(i2.id)
    })

    private val _shopListLiveData = MutableLiveData<List<ShopListItem>>()

    init {
        addShopItem(ShopListItem("one", 1, true))
        addShopItem(ShopListItem("two", 2, true))
        addShopItem(ShopListItem("three", 3, true))
    }

    private var lastId = 0

    override fun addShopItem(item: ShopListItem) {
        if (item.id == ShopListItem.UNREGISTERED_ID) {
            item.id = ++lastId
        }

        shopList.add(item)
        updateShopList() // notify observers via LiveData
    }

    override fun getShopItem(id: Int): ShopListItem? {
        return shopList.find { item -> item.id == id }
    }

    override fun editShopItem(item: ShopListItem) {
        removeShopItem(item.id)
        addShopItem(item)
    }

    override fun removeShopItem(itemId: Int) {
        shopList.removeIf { it.id == itemId }

        updateShopList() // notify observers via LiveData
    }

    override fun removeShopItem(item: ShopListItem) {
        shopList.remove(item)

        updateShopList() // notify observers via LiveData
    }

    override fun getShopListLD(): LiveData<List<ShopListItem>> {
        return _shopListLiveData
    }

    private fun updateShopList() {
        _shopListLiveData.value = shopList.toList()
    }
}