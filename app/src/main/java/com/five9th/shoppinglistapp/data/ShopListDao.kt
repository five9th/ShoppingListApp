package com.five9th.shoppinglistapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_items")
    fun getShopList(): LiveData<List<ShopListItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertShopItem(item: ShopListItemDbModel)

    @Query("DELETE FROM shop_items WHERE id=:itemId")
    fun deleteShopItem(itemId: Int)

    @Query("SELECT * FROM shop_items WHERE id=:itemId")
    fun getShopItem(itemId: Int): ShopListItemDbModel
}