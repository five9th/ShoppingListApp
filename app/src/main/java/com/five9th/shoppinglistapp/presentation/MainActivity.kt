package com.five9th.shoppinglistapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.five9th.shoppinglistapp.R
import com.five9th.shoppinglistapp.domain.entity.ShopListItem


class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"

    private lateinit var mainViewModel: MainViewModel

    private lateinit var shopListRv: RecyclerView
    private lateinit var shopListAdapter: ShopListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        initViewModel()
        setupShopListRv()
    }

    private fun findViews() {
        shopListRv = findViewById(R.id.main_rv)
    }

    private fun initViewModel() {
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        mainViewModel.getShopListLD().observe(this, ::onShopListChanged)
    }

    private fun setupShopListRv() {
        // set adapter
        shopListAdapter = ShopListAdapter()
        shopListRv.adapter = shopListAdapter

        // set up pool
        shopListRv.recycledViewPool.also {
            it.setMaxRecycledViews(
                ShopListAdapter.ITEM_VIEW_TYPE_ENABLED, ShopListAdapter.MAX_POOL_SIZE)
            it.setMaxRecycledViews(
                ShopListAdapter.ITEM_VIEW_TYPE_DISABLED, ShopListAdapter.MAX_POOL_SIZE)
        }

        // set listeners
        shopListAdapter.onClickListener = ::onItemClick
        shopListAdapter.onLongClickListener = ::onLongItemClick
    }


    private fun onShopListChanged(shopList: List<ShopListItem>) {
        Log.d(tag, "onShopListChanged: $shopList")
        shopListAdapter.shopList = shopList
    }

    /** Changing enable state */
    private fun onLongItemClick(item: ShopListItem) {
        mainViewModel.changeEnableState(item)
    }

    /** Open edit item activity */
    private fun onItemClick(item: ShopListItem) {
        Log.d(tag, "onItemClick: $item")
        // TODO: Open edit item activity
    }

}