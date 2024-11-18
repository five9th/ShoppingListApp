package com.five9th.shoppinglistapp.domain.usecase

import androidx.lifecycle.LiveData
import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class GetShopListUsecase(private val shopListRepository: ShopListRepository) {

    fun getShopListLD(): LiveData<List<ShopListItem>> {
        return shopListRepository.getShopListLD()
    }
}