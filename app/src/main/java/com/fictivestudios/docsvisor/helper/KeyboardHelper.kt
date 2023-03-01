package com.fictivestudios.docsvisor.helper

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText


object KeyboardHelper {

    fun hideSoftKeyboard(context: Context?, view: View?) {
        if (context == null || view == null) {
            return
        }
        val imm: InputMethodManager = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }


    fun hideSoftKeyboardForced(context: Context?, view: View) {
        if (context == null) {
            return
        }
        val imm: InputMethodManager = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0)
    }

    fun hideSoftKeyboard(context: Context?, editText: EditText) {
        if (context == null) {
            return
        }
        val imm: InputMethodManager = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            editText.windowToken,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    fun showSoftKeyboard(context: Context?, editText: AppCompatEditText) {
        if (context == null) {
            return
        }
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
        editText.requestFocus()
    }

    fun showSoftKeyboardForcefully(context: Context?, editText: EditText) {
        if (context == null) {
            return
        }
        editText.requestFocus()
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED)
        editText.requestFocus()
    }
}
