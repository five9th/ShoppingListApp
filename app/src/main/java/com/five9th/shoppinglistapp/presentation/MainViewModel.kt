package com.five9th.shoppinglistapp.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.five9th.shoppinglistapp.data.ShopListRepositoryImpl
import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import com.five9th.shoppinglistapp.domain.usecase.AddShopItemUsecase
import com.five9th.shoppinglistapp.domain.usecase.EditShopItemUsecase
import com.five9th.shoppinglistapp.domain.usecase.GetShopListUsecase
import com.five9th.shoppinglistapp.domain.usecase.RemoveShopItemUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ShopListRepositoryImpl(application) // TODO: it's bad to init repository in here

    private val getShopListUsecase = GetShopListUsecase(repository)
    private val removeShopItemUsecase = RemoveShopItemUsecase(repository)
    private val addShopItemUsecase = AddShopItemUsecase(repository)
    private val editShopItemUsecase = EditShopItemUsecase(repository)


    fun getShopListLD(): LiveData<List<ShopListItem>> {
        return getShopListUsecase.getShopListLD()
    }

    fun removeShopItem(item: ShopListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            removeShopItemUsecase.removeShopItem(item.id)
        }
    }

    fun addShopItem(item: ShopListItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addShopItemUsecase.addShopItem(item)
        }
    }

    fun changeEnableState(item: ShopListItem) {
        val newItem = item.copy(isEnabled = !item.isEnabled)

        viewModelScope.launch(Dispatchers.IO) {
            editShopItemUsecase.editShopItem(newItem)
        }
    }
}