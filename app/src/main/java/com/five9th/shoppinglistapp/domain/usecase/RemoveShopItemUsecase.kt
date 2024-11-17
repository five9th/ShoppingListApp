package com.five9th.shoppinglistapp.domain.usecase

class RemoveShopItemUsecase(private val shopListRepository: ShoplistRepository) {
    fun removeShopItem(itemId: Int) {
        shopListRepository.removeShopItem(itemId)
    }
}