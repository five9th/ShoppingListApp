package com.five9th.shoppinglistapp.presentation

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.five9th.shoppinglistapp.R
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Number) {
    textView.text = String.format(Locale.getDefault(), "%d", number)
}

@BindingAdapter("nameHasError")
fun bindNameHasError(textInputLayout: TextInputLayout, hasError: Boolean) {
    textInputLayout.error =
        if (hasError) ContextCompat.getString(textInputLayout.context, R.string.wrong_name)
        else null
}

@BindingAdapter("countHasError")
fun bindCountHasError(textInputLayout: TextInputLayout, hasError: Boolean) {
    textInputLayout.error =
        if (hasError) ContextCompat.getString(textInputLayout.context, R.string.wrong_value)
        else null
}