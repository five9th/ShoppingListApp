package com.five9th.shoppinglistapp.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ShopListItemDbModel::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getShopListDao(): ShopListDao

    companion object {
        private const val DB_NAME = "shop_item.db"

        private var instance: AppDataBase? = null
        private var lock = Any()

        fun getInstance(application: Application): AppDataBase {
            return instance ?: synchronized(lock) {
                instance ?: createDb(application).also {
                    instance = it
                }
            }
        }

        private fun createDb(application: Application): AppDataBase {
            return Room.databaseBuilder(
                application,
                AppDataBase::class.java,
                DB_NAME
            ).allowMainThreadQueries() // TODO: for testing only, remove this later
                .build()
        }
    }
}