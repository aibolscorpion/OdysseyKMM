package kz.divtech.odyssey.rotation.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kz.divtech.odyssey.rotation.R
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
    fun showKeyboard(context: Context, editText: EditText){
        editText.requestFocus()
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    fun hideKeyboard(context: Context, view : View){
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

    }

    fun getIntFromString(message: String): Int{
        val sB = StringBuilder()
        message.forEach { char ->
            try {
                if(Integer.valueOf(char.toString()) in 0..9){
                    sB.append(char)
                }
            }catch (_: NumberFormatException){
            }
        }
        return Integer.valueOf(sB.toString())
    }

    fun showErrorMessage(context: Context, view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val params = snackBar.view.layoutParams as FrameLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        snackBar.view.layoutParams = params
        snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.bottom_sheet_error_title))
        snackBar.show()
    }

    fun formatDateTime(serverDateTime: String?): String {
        val serverDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val format = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
        val parsedDateTime = LocalDateTime.parse(serverDateTime, serverDateTimeFormat)
        return parsedDateTime.format(format)
    }

}