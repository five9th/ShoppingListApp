package com.five9th.shoppinglistapp.data

import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class ShopListMapper {

    fun mapEntityToDbModel(shopListItem: ShopListItem) =
        ShopListItemDbModel(
            id = shopListItem.id,
            name = shopListItem.name,
            count = shopListItem.count,
            isEnabled = shopListItem.isEnabled
        )

    fun mapDbModelToEntity(shopListItemDbModel: ShopListItemDbModel) =
        ShopListItem(
            id = shopListItemDbModel.id,
            name = shopListItemDbModel.name,
            count = shopListItemDbModel.count,
            isEnabled = shopListItemDbModel.isEnabled
        )

    fun mapDbModelListToEntityList(list: List<ShopListItemDbModel>) = list.map {
        mapDbModelToEntity(it)
    }
}