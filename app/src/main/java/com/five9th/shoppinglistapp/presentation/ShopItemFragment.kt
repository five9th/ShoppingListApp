package com.five9th.shoppinglistapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.five9th.shoppinglistapp.R
import com.five9th.shoppinglistapp.domain.entity.ShopListItem
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment(
    private val mode: Int = MODE_UNDEFINED,
    private val itemId: Int = ID_UNDEFINED
) : Fragment() {

    private val tag = "ShopItemFragment"

    private lateinit var itemNameTil: TextInputLayout
    private lateinit var itemCountTil: TextInputLayout
    private lateinit var itemNameEt: EditText
    private lateinit var itemCountEt: EditText
    private lateinit var saveBtn: Button

    private lateinit var viewModel: ShopItemViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(tag, "onCreateView") ////
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(tag, "onViewCreated, mode: $mode, id: $itemId") ////

        findViews(view)
        setListeners()

        initViewModel()
        subscribeForLD()

        parseParams()
    }

    private fun findViews(view: View) {
        itemNameTil = view.findViewById(R.id.item_name_til)
        itemCountTil = view.findViewById(R.id.item_count_til)
        itemNameEt = view.findViewById(R.id.item_name_et)
        itemCountEt = view.findViewById(R.id.item_count_et)
        saveBtn = view.findViewById(R.id.save_btn)
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
        viewModel.shopItemLD.observe(viewLifecycleOwner) {
            Log.d(tag, "shopItemLD update: $it") ////
            itemNameEt.setText(it.name)
            itemCountEt.setText(it.count.toString())
        }

        viewModel.nameHasError.observe(viewLifecycleOwner) {
            if (it) itemNameTil.setError(getString(R.string.wrong_name))
            else itemNameTil.error = null
        }

        viewModel.countHasError.observe(viewLifecycleOwner) {
            if (it) itemCountTil.setError(getString(R.string.wrong_value))
            else itemCountTil.error = null
        }

        viewModel.closeActivity.observe(viewLifecycleOwner) {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
    }

    private fun parseParams() {
        Log.d(tag, "parseParams, mode: $mode, id: $itemId") ////

        when (mode) {
            MODE_ADD -> initForAdd()
            MODE_EDIT -> initForEdit()
            else -> throw RuntimeException("Unknown mode: $mode")
        }
    }

    private fun initForAdd() {
        // do nothing ...
    }

    private fun initForEdit() {
        if (itemId == ID_UNDEFINED) throw RuntimeException("Undefined item id")

        viewModel.getShopItem(itemId)
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
        private const val MODE_ADD = 1
        private const val MODE_EDIT = 2
        private const val MODE_UNDEFINED = -1

        private const val ID_UNDEFINED = ShopListItem.UNREGISTERED_ID

        fun newInstanceAdd(): ShopItemFragment {
            Log.d("ShopItemFragment", "newInstanceAdd") ////
            return ShopItemFragment(MODE_ADD)
        }

        fun newInstanceEdit(itemId: Int): ShopItemFragment {
            Log.d("ShopItemFragment", "newInstanceEdit") ////
            return ShopItemFragment(MODE_EDIT, itemId)
        }
    }
}