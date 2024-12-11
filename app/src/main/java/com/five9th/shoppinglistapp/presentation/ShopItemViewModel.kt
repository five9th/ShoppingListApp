package com.five9th.shoppinglistapp.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.five9th.shoppinglistapp.data.ShopListRepositoryImpl
import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import com.five9th.shoppinglistapp.domain.usecase.AddShopItemUsecase
import com.five9th.shoppinglistapp.domain.usecase.EditShopItemUsecase
import com.five9th.shoppinglistapp.domain.usecase.GetShopItemUsecase

class ShopItemViewModel : ViewModel() {
    private val repository = ShopListRepositoryImpl // TODO: it's bad to init repository in here

    private val getShopItemUsecase = GetShopItemUsecase(repository)
    private val addShopItemUsecase = AddShopItemUsecase(repository)
    private val editShopItemUsecase = EditShopItemUsecase(repository)

    private val _shopItemLD = MutableLiveData<ShopListItem>()
    val shopItemLD: LiveData<ShopListItem>
        get() = _shopItemLD

    private val _nameHasError = MutableLiveData<Boolean>()
    val nameHasError: LiveData<Boolean>
        get() = _nameHasError

    private val _countHasError = MutableLiveData<Boolean>()
    val countHasError: LiveData<Boolean>
        get() = _countHasError

    private val _closeActivity = MutableLiveData<Unit>()
    val closeActivity: LiveData<Unit>
        get() = _closeActivity


    fun getShopItem(itemId: Int) {
        val item = getShopItemUsecase.getShopItem(itemId)
        item?.also {
            _shopItemLD.postValue(item)
        }
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        val isValid = validateInput(name, count)

        if (isValid) {
            val shopItem = ShopListItem(name, count, true)
            addShopItemUsecase.addShopItem(shopItem)

            // Notify that ShopItemActivity may me closed
            _closeActivity.value = Unit
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)

        val isValid = validateInput(name, count)

        if (isValid) {
            val shopItem = shopItemLD.value

            shopItem?.let {
                val editedItem = it.copy(name = name, count = count)

                editShopItemUsecase.editShopItem(editedItem)

                // Notify that ShopItemActivity may me closed
                _closeActivity.value = Unit
            }
        }
    }

    private fun parseName(name: String?): String {
        return name?.trim() ?: ""
    }

    private fun parseCount(count: String?): Int {
        return count?.toIntOrNull() ?: 0
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var isValid = true

        if (name.isBlank()) {
            isValid = false
            // show name error
            _nameHasError.value = true
        }

        if (count <= 0) {
            isValid = false
            // show count error
            _countHasError.value = true
        }

        return isValid
    }

    fun resetNameError() {
        _nameHasError.postValue(false)
    }

    fun resetCountError() {
        _countHasError.postValue(false)
    }
}