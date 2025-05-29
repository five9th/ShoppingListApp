package com.five9th.shoppinglistapp.domain.usecase

import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class GetShopItemUsecase(private val shopListRepository: ShopListRepository) {

    fun getShopItem(itemId: Int): ShopListItem? {
        return shopListRepository.getShopItem(itemId)
    }
}