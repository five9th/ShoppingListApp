package com.five9th.shoppinglistapp.domain.usecase

class RemoveShopItemUsecase(private val shopListRepository: ShopListRepository) {
    suspend fun removeShopItem(itemId: Int) {
        shopListRepository.removeShopItem(itemId)
    }
}