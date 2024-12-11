package com.five9th.shoppinglistapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.five9th.shoppinglistapp.R

class ShopItemActivity : AppCompatActivity() {

    private val tag = "ShopItemActivity"

    private lateinit var viewModel: ShopItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViewModel()
        getValuesFromIntent()
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
    }

    private fun getValuesFromIntent() {
        val mode = intent.getIntExtra(EXTRA_MODE, -1)
        val id = intent.getIntExtra(EXTRA_ITEM_ID, -100)

        Log.d(tag, "mode: $mode, id: $id")
    }

    companion object {
        private const val EXTRA_MODE = "extra_mode"
        private const val MODE_ADD = 1
        private const val MODE_EDIT = 2

        private const val EXTRA_ITEM_ID = "extra_item_id"

        fun newIntentAddItem(context: Context): Intent {
            val newIntent = Intent(context, ShopItemActivity::class.java)
            newIntent.putExtra(EXTRA_MODE, MODE_ADD)

            return newIntent
        }

        fun newIntentEditItem(context: Context, itemId: Int): Intent {
            val newIntent = Intent(context, ShopItemActivity::class.java)
            newIntent.putExtra(EXTRA_MODE, MODE_EDIT)
            newIntent.putExtra(EXTRA_ITEM_ID, itemId)

            return newIntent
        }
    }
}