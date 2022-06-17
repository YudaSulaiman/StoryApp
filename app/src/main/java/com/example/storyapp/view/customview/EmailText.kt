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

class EmailText: AppCompatEditText {

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
        hint = context.getString(R.string.input_email)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
    }

    private fun init() {

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Do nothing.
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val binding = rootView.findViewById<TextInputLayout>(R.id.emailEditTextLayout)
                when {
                    (s.isEmpty()) -> {
                        binding.error = context.getString(R.string.empty_warning)
                    }
                    (!android.util.Patterns.EMAIL_ADDRESS.matcher(s).matches()) -> {
                        binding.error = context.getString(R.string.email_warning)
                    }
                    else ->
                        binding.error = null
                }
            }
            override fun afterTextChanged(s: Editable) {
                // Do nothing.
            }
        })
    }
}