package com.five9th.shoppinglistapp.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.ViewModelProvider
import com.five9th.shoppinglistapp.R
import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {

    private val tag = "ShopItemActivity"

    private lateinit var itemNameTil: TextInputLayout
    private lateinit var itemCountTil: TextInputLayout
    private lateinit var itemNameEt: EditText
    private lateinit var itemCountEt: EditText
    private lateinit var saveBtn: Button

    private lateinit var viewModel: ShopItemViewModel

    private var mode = MODE_UNDEFINED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge() // Doesn't work with windowSoftInputMode="adjustResize"
        setContentView(R.layout.activity_shop_item)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViews()
        setListeners()

        initViewModel()
        subscribeForLD()

        parseIntent()
    }

    private fun findViews() {
        itemNameTil = findViewById(R.id.item_name_til)
        itemCountTil = findViewById(R.id.item_count_til)
        itemNameEt = findViewById(R.id.item_name_et)
        itemCountEt = findViewById(R.id.item_count_et)
        saveBtn = findViewById(R.id.save_btn)
    }

    private fun setListeners() {
        saveBtn.setOnClickListener(::onSaveBtnClick)

        itemNameEt.doAfterTextChanged {
            viewModel.resetNameError()
        }

        itemCountEt.doAfterTextChanged {
            viewModel.resetCountError()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]
    }

    private fun subscribeForLD() {
        viewModel.shopItemLD.observe(this) {
            Log.d(tag, "shopItemLD update: $it") ////
            itemNameEt.setText(it.name)
            itemCountEt.setText(it.count.toString())
        }

        viewModel.nameHasError.observe(this) {
            if (it) itemNameTil.setError(getString(R.string.wrong_name))
            else itemNameTil.error = null
        }

        viewModel.countHasError.observe(this) {
            if (it) itemCountTil.setError(getString(R.string.wrong_value))
            else itemCountTil.error = null
        }

        viewModel.closeActivity.observe(this) {
            finish()
        }
    }

    private fun parseIntent() {
        val mode = intent.getIntExtra(EXTRA_MODE, MODE_UNDEFINED)

        when (mode) {
            MODE_ADD -> initForAdd()
            MODE_EDIT -> initForEdit()
            else -> throw RuntimeException("Unknown mode: $mode")
        }
    }

    private fun initForAdd() {
        mode = MODE_ADD
    }

    private fun initForEdit() {
        mode = MODE_EDIT

        val id = intent.getIntExtra(EXTRA_ITEM_ID, ID_UNDEFINED)
        if (id == ID_UNDEFINED) throw RuntimeException("Undefined item id")

        viewModel.getShopItem(id)
    }


    private fun onSaveBtnClick(view: View) {
        val name = itemNameEt.text?.toString()
        val count = itemCountEt.text?.toString()

        when (mode) {
            MODE_ADD -> viewModel.tryAddShopItem(name, count)
            MODE_EDIT -> viewModel.tryEditShopItem(name, count)
        }
    }


    companion object {
        private const val EXTRA_MODE = "extra_mode"
        private const val MODE_ADD = 1
        private const val MODE_EDIT = 2
        private const val MODE_UNDEFINED = -1

        private const val EXTRA_ITEM_ID = "extra_item_id"
        private const val ID_UNDEFINED = ShopListItem.UNREGISTERED_ID

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