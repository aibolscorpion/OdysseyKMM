package kz.divtech.odyssey.rotation.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object Utils {
    fun showKeyboard(context: Context, editText: EditText){
        editText.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }
}