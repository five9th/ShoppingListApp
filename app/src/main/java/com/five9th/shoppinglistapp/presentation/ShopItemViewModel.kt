package com.five9th.shoppinglistapp.presentation

import androidx.lifecycle.ViewModel
import com.five9th.shoppinglistapp.data.ShopListRepositoryImpl
import com.five9th.shoppinglistapp.domain.usecase.AddShopItemUsecase
import com.five9th.shoppinglistapp.domain.usecase.EditShopItemUsecase
import com.five9th.shoppinglistapp.domain.usecase.GetShopListUsecase

class ShopItemViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl // TODO: it's bad to init repository in here

    private val getShopItemUsecase = GetShopListUsecase(repository)
    private val addShopItemUsecase = AddShopItemUsecase(repository)
    private val editShopItemUsecase = EditShopItemUsecase(repository)
}