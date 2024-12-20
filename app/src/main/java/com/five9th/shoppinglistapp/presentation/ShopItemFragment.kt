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

class ShopItemFragment : Fragment() {

    private val tag = "ShopItemFragment"

    private var fragmentMode: Int = MODE_UNDEFINED
    private var itemId: Int = ID_UNDEFINED

    private lateinit var itemNameTil: TextInputLayout
    private lateinit var itemCountTil: TextInputLayout
    private lateinit var itemNameEt: EditText
    private lateinit var itemCountEt: EditText
    private lateinit var saveBtn: Button

    private lateinit var viewModel: ShopItemViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        findViews(view)
        setListeners()

        initViewModel()
        subscribeForLD()

        launchRightMode()
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

    /** Sets [fragmentMode] and [itemId] fields */
    private fun parseArgs() {
        val mode = requireArguments().getInt(MODE_KEY, MODE_UNDEFINED)

        when (mode) {
            MODE_ADD -> initForAdd()
            MODE_EDIT -> initForEdit()
            else -> throw RuntimeException("Unknown mode: $mode")
        }
    }

    private fun initForAdd() {
        fragmentMode = MODE_ADD
    }

    private fun initForEdit() {
        val id = requireArguments().getInt(ITEM_ID_KEY, ID_UNDEFINED)
        if (id == ID_UNDEFINED) throw RuntimeException("Undefined item id")

        itemId = id
        fragmentMode = MODE_EDIT
    }

    private fun launchRightMode() {
        when (fragmentMode) {
            MODE_ADD -> launchAddMode()
            MODE_EDIT -> launchEditMode()
        }
    }

    private fun launchAddMode() {
        // nothing needs to be done
    }

    private fun launchEditMode() {
        // request item from viewmodel
        viewModel.getShopItem(itemId)

        // listening LiveData for Item
        viewModel.shopItemLD.observe(viewLifecycleOwner) {
            Log.d(tag, "shopItemLD: $it") ////
            itemNameEt.setText(it.name)
            itemCountEt.setText(it.count.toString())
        }
    }

    private fun onSaveBtnClick(view: View) {
        val name = itemNameEt.text?.toString()
        val count = itemCountEt.text?.toString()

        when (fragmentMode) {
            MODE_ADD -> viewModel.tryAddShopItem(name, count)
            MODE_EDIT -> viewModel.tryEditShopItem(name, count)
        }
    }


    companion object {
        private const val MODE_KEY = "mode"
        private const val MODE_ADD = 1
        private const val MODE_EDIT = 2
        private const val MODE_UNDEFINED = -1

        private const val ITEM_ID_KEY = "item_id"
        private const val ID_UNDEFINED = ShopListItem.UNREGISTERED_ID

        fun newInstanceAdd(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(MODE_KEY, MODE_ADD)
                }
            }
        }

        fun newInstanceEdit(itemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(MODE_KEY, MODE_EDIT)
                    putInt(ITEM_ID_KEY, itemId)
                }
            }
        }
    }
}