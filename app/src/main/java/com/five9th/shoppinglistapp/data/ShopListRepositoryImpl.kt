package com.five9th.shoppinglistapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import com.five9th.shoppinglistapp.domain.usecase.ShopListRepository


// TODO: perform DB operations in another thread
class ShopListRepositoryImpl(app: Application) : ShopListRepository {

    private val dao = AppDataBase.getInstance(app).getShopListDao()
    private val mapper = ShopListMapper()

    override suspend fun addShopItem(item: ShopListItem) {
        item.id = 0 // id must be 0 for Room to treat it as unset

        val dbModel = mapper.mapEntityToDbModel(item)
        dao.insertShopItem(dbModel)
    }

    override suspend fun getShopItem(id: Int): ShopListItem? { // TODO: what if id not found
        val dbModel = dao.getShopItem(id)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override suspend fun editShopItem(item: ShopListItem) {
        val dbModel = mapper.mapEntityToDbModel(item)
        dao.insertShopItem(dbModel)
    }

    override suspend fun removeShopItem(itemId: Int) {
        dao.deleteShopItem(itemId)
    }

    override suspend fun removeShopItem(item: ShopListItem) {
        dao.deleteShopItem(item.id)
    }

    override fun getShopListLD(): LiveData<List<ShopListItem>> = dao.getShopList().map { list ->
        mapper.mapDbModelListToEntityList(list)
    }
}