package com.five9th.shoppinglistapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

import com.five9th.shoppinglistapp.data.ShopListRepositoryImpl
import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import com.five9th.shoppinglistapp.domain.usecase.AddShopItemUsecase
import com.five9th.shoppinglistapp.domain.usecase.EditShopItemUsecase
import com.five9th.shoppinglistapp.domain.usecase.GetShopListUsecase
import com.five9th.shoppinglistapp.domain.usecase.RemoveShopItemUsecase

class MainViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl // TODO: it's bad to init repository in here

    private val getShopListUsecase = GetShopListUsecase(repository)
    private val removeShopItemUsecase = RemoveShopItemUsecase(repository)
    private val addShopItemUsecase = AddShopItemUsecase(repository)
    private val editShopItemUsecase = EditShopItemUsecase(repository)

    fun getShopListLD(): LiveData<List<ShopListItem>> {
        return getShopListUsecase.getShopListLD()
    }

    fun removeShopItem(item: ShopListItem) {
        removeShopItemUsecase.removeShopItem(item.id)
    }

    fun addShopItem(item: ShopListItem) {
        addShopItemUsecase.addShopItem(item)
    }

    fun changeEnableState(item: ShopListItem) {
        val newItem = item.copy(isEnabled = !item.isEnabled)
        editShopItemUsecase.editShopItem(newItem)
    }
}