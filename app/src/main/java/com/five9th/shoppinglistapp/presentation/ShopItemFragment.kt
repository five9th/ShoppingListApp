package com.five9th.shoppinglistapp.presentation

import android.content.Context
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

    private lateinit var editingFinishedListener: EditingFinishedListener

    private var fragmentMode: Int = MODE_UNDEFINED
    private var itemId: Int = ID_UNDEFINED

    private lateinit var itemNameTil: TextInputLayout
    private lateinit var itemCountTil: TextInputLayout
    private lateinit var itemNameEt: EditText
    private lateinit var itemCountEt: EditText
    private lateinit var saveBtn: Button

    private lateinit var viewModel: ShopItemViewModel

    init {
        Log.d(tag, "Constructor")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(tag, "onAttach")

        if (context is EditingFinishedListener) {
            editingFinishedListener = context
        }
        else {
            throw Exception("$context must implement an EditingFinishedListener interface")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate")

        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(tag, "onCreateView")

        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(tag, "onViewCreated")

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
            editingFinishedListener.onEditingFinished()
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


    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(tag, "onViewStateRestored")
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(tag, "onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(tag, "onDetach")
    }


    interface EditingFinishedListener {
        fun onEditingFinished()
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