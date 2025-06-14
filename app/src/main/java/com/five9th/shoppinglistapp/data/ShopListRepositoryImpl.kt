package com.five9th.shoppinglistapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import com.five9th.shoppinglistapp.domain.usecase.ShopListRepository


// TODO: perform DB operations in another thread
class ShopListRepositoryImpl(app: Application) : ShopListRepository {

    private val dao = AppDataBase.getInstance(app).getShopListDao()
    private val mapper = ShopListMapper()

    override fun addShopItem(item: ShopListItem) {
        val dbModel = mapper.mapEntityToDbModel(item)
        dao.insertShopItem(dbModel)
    }

    override fun getShopItem(id: Int): ShopListItem? { // TODO: what if id not found
        val dbModel = dao.getShopItem(id)
        return mapper.mapDbModelToEntity(dbModel)
    }

    override fun editShopItem(item: ShopListItem) {
        val dbModel = mapper.mapEntityToDbModel(item)
        dao.insertShopItem(dbModel)
    }

    override fun removeShopItem(itemId: Int) {
        dao.deleteShopItem(itemId)
    }

    override fun removeShopItem(item: ShopListItem) {
        dao.deleteShopItem(item.id)
    }

    override fun getShopListLD(): LiveData<List<ShopListItem>> {
        return TODO("figure out how to use LiveData here")
    }
}