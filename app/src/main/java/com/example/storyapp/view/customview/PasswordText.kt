package com.example.storyapp.view.customview

import android.content.Context
import android.graphics.Canvas
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import com.example.storyapp.R
import com.google.android.material.textfield.TextInputLayout

class PasswordText: AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = context.getString(R.string.input_password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                val binding = rootView.findViewById<TextInputLayout>(R.id.passwordEditTextLayout)
                when {
                    (s.isEmpty()) -> {
                        binding.error = context.getString(R.string.empty_warning)
                    }
                    (s.length < 6) -> {
                        binding.error = context.getString(R.string.password_warning)
                    }
                    else -> binding.error = null
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}