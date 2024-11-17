package com.five9th.shoppinglistapp.domain.entity

data class ShopListItem(
    val id: Int,
    val name: String,
    val count: Int,
    val isEnabled: Boolean
)
