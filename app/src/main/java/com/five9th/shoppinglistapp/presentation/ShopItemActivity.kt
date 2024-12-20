package com.five9th.shoppinglistapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.five9th.shoppinglistapp.R
import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class ShopItemActivity : AppCompatActivity() {

    private val tag = "ShopItemActivity"

    private var screenMode = MODE_UNDEFINED
    private var itemId = ID_UNDEFINED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() // Doesn't work with windowSoftInputMode="adjustResize"
        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.shop_item_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        Log.d(tag, "onCreate") ////

        parseIntent()
        setUpFragment()
    }

    /** Sets the [screenMode] and [itemId] fields */
    private fun parseIntent() {
        val mode = intent.getIntExtra(EXTRA_MODE, MODE_UNDEFINED)
        Log.d(tag, "parseIntent, mode: $mode") ////

        when (mode) {
            MODE_ADD -> initForAdd()
            MODE_EDIT -> initForEdit()
            else -> throw RuntimeException("Unknown mode: $mode")
        }
    }

    private fun initForAdd() {
        screenMode = MODE_ADD
    }

    private fun initForEdit() {
        screenMode = MODE_EDIT

        val id = intent.getIntExtra(EXTRA_ITEM_ID, ID_UNDEFINED)
        if (id == ID_UNDEFINED) throw RuntimeException("Undefined item id")

        itemId = id
    }

    /** Sets fragment to the container */
    private fun setUpFragment() {
        Log.d(tag, "setUpFragment, mode: $screenMode") ////

        val fragment = when(screenMode) {
            MODE_ADD -> ShopItemFragment.newInstanceAdd()
            MODE_EDIT -> ShopItemFragment.newInstanceEdit(itemId)
            else -> throw RuntimeException("Unknown mode: $screenMode")
        }

        supportFragmentManager.beginTransaction()
            .add(R.id.shop_item_container, fragment)
            .commit()
    }


    companion object {
        private const val EXTRA_MODE = "extra_mode"
        private const val MODE_ADD = 1
        private const val MODE_EDIT = 2
        private const val MODE_UNDEFINED = -1

        private const val EXTRA_ITEM_ID = "extra_item_id"
        private const val ID_UNDEFINED = ShopListItem.UNREGISTERED_ID

        fun newIntentAddItem(context: Context): Intent {
            Log.d("ShopItemActivity", "newIntentAddItem") ////
            val newIntent = Intent(context, ShopItemActivity::class.java)
            newIntent.putExtra(EXTRA_MODE, MODE_ADD)

            return newIntent
        }

        fun newIntentEditItem(context: Context, itemId: Int): Intent {
            Log.d("ShopItemActivity", "newIntentEditItem") ////
            val newIntent = Intent(context, ShopItemActivity::class.java)
            newIntent.putExtra(EXTRA_MODE, MODE_EDIT)
            newIntent.putExtra(EXTRA_ITEM_ID, itemId)

            return newIntent
        }
    }
}