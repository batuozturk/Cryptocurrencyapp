package com.batuhan.cryptocurrencyapp.core.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class CryptoCurrencyEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    var eventHandler: CryptoCurrencyEditTextEventHandler? = null

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        super.onSelectionChanged(selStart, selEnd)
        if (selStart == selEnd) eventHandler?.onCursorSelectionChanged(selStart)
    }
}

interface CryptoCurrencyEditTextEventHandler {

    fun onCursorSelectionChanged(cursorPosition: Int)
}
