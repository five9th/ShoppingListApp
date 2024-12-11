package com.five9th.shoppinglistapp.presentation

import androidx.lifecycle.ViewModel
import com.five9th.shoppinglistapp.data.ShopListRepositoryImpl
import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import com.five9th.shoppinglistapp.domain.usecase.AddShopItemUsecase
import com.five9th.shoppinglistapp.domain.usecase.EditShopItemUsecase
import com.five9th.shoppinglistapp.domain.usecase.GetShopItemUsecase

class ShopItemViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl // TODO: it's bad to init repository in here

    private val getShopItemUsecase = GetShopItemUsecase(repository)
    private val addShopItemUsecase = AddShopItemUsecase(repository)
    private val editShopItemUsecase = EditShopItemUsecase(repository)

    fun getShopItem(itemId: Int): ShopListItem? {
        return getShopItemUsecase.getShopItem(itemId)
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        val isValid = validateInput(name, count)

        if (isValid) {
            val shopItem = ShopListItem(name, count, true)
            addShopItemUsecase.addShopItem(shopItem)
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        val isValid = validateInput(name, count)

        if (isValid) {
            // TODO: get shopItem by id from repository, not create new one
            val shopItem = ShopListItem(name, count, true)
            editShopItemUsecase.editShopItem(shopItem)
        }
    }

    private fun parseName(name: String?): String {
        return name?.trim() ?: ""
    }

    private fun parseCount(count: String?): Int {
        return count?.toIntOrNull() ?: 0
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var isValid = true

        if (name.isBlank()) {
            isValid = false
            // TODO: show name error
        }

        if (count <= 0) {
            isValid = false
            // TODO: show count error
        }

        return isValid
    }
}