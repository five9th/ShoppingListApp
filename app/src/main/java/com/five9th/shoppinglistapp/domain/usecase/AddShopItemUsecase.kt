package com.five9th.shoppinglistapp.domain.usecase

import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class AddShopItemUsecase(private val shopListRepository: ShopListRepository) {
    suspend fun addShopItem(item: ShopListItem) {
        shopListRepository.addShopItem(item)
    }
}