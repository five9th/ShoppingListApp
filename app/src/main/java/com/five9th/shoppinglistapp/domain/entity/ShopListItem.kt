package com.five9th.shoppinglistapp.domain.entity

data class ShopListItem(
    val name: String,
    val count: Int,
    val isEnabled: Boolean,
    var id: Int = UNREGISTERED_ID
) {
    companion object {
        const val UNREGISTERED_ID = -1
    }
}
