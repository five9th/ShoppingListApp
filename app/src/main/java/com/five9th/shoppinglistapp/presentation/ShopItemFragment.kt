package com.five9th.shoppinglistapp.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.five9th.shoppinglistapp.databinding.FragmentShopItemBinding
import com.five9th.shoppinglistapp.domain.entity.ShopListItem

class ShopItemFragment : Fragment() {

    private val tag = "ShopItemFragment"

    private lateinit var editingFinishedListener: EditingFinishedListener

    private var fragmentMode: Int = MODE_UNDEFINED
    private var itemId: Int = ID_UNDEFINED

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding!!

    private lateinit var viewModel: ShopItemViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is EditingFinishedListener) {
            editingFinishedListener = context
        }
        else {
            throw Exception("$context must implement an EditingFinishedListener interface")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()

        initViewModel()
        subscribeForLD()

        launchRightMode()
    }

    private fun setListeners() {
        binding.saveBtn.setOnClickListener(::onSaveBtnClick)

        binding.itemNameEt.doAfterTextChanged {
            viewModel.resetNameError()
        }

        binding.itemCountEt.doAfterTextChanged {
            viewModel.resetCountError()
        }
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this)[ShopItemViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    private fun subscribeForLD() {
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
    }

    private fun onSaveBtnClick(view: View) {
        val name = binding.itemNameEt.text?.toString()
        val count = binding.itemCountEt.text?.toString()

        when (fragmentMode) {
            MODE_ADD -> viewModel.tryAddShopItem(name, count)
            MODE_EDIT -> viewModel.tryEditShopItem(name, count)
        }
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