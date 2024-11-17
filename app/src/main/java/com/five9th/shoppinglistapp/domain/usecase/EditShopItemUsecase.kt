package com.five9th.shoppinglistapp.domain.usecase

import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class EditShopItemUsecase(private val shopListRepository: ShoplistRepository) {
    fun editShopItem(item: ShopListItem) {
        shopListRepository.editShopItem(item)
    }
}