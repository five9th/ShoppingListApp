package com.five9th.shoppinglistapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.five9th.shoppinglistapp.R
import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    private val tag = "MainActivity"

    private lateinit var mainViewModel: MainViewModel

    private lateinit var shopListRv: RecyclerView
    private lateinit var shopListAdapter: ShopListAdapter

    private lateinit var addFab: FloatingActionButton

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
        setListeners()
    }

    private fun findViews() {
        shopListRv = findViewById(R.id.main_rv)
        addFab = findViewById(R.id.add_fab)
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

        // set swipe to delete
        setSwipeToDelete()
    }

    private fun setSwipeToDelete() {
        val swipeCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                // Drag-and-drop is disabled; return false
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val itemToRemove = shopListAdapter.currentList[position]
                mainViewModel.removeShopItem(itemToRemove)
            }
        }

        // Attach ItemTouchHelper to RecyclerView
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(shopListRv)
    }

    private fun setListeners() {
        addFab.setOnClickListener(::onAddBtnClick)
    }


    private fun onShopListChanged(shopList: List<ShopListItem>) {
        Log.d(tag, "onShopListChanged: $shopList")
        shopListAdapter.submitList(shopList)
    }

    /** Changing enable state */
    private fun onLongItemClick(item: ShopListItem) {
        mainViewModel.changeEnableState(item)
    }

    /** Open edit item activity */
    private fun onItemClick(item: ShopListItem) {
        Log.d(tag, "onItemClick: $item")

        val intent = ShopItemActivity.newIntentEditItem(this, item.id)
        startActivity(intent)
    }

    private fun onAddBtnClick(view: View) {
        val intent = ShopItemActivity.newIntentAddItem(this)
        startActivity(intent)
    }

}