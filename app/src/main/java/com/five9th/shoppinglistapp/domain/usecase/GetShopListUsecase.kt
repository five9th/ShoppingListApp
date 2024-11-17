package com.five9th.shoppinglistapp.domain.usecase

import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class GetShopListUsecase(private val shopListRepository: ShoplistRepository) {

    fun getShopList(): List<ShopListItem> {
        return shopListRepository.getShopList()
    }
}