package kz.divtech.odyssey.rotation.common.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kz.divtech.odyssey.rotation.R

object InputUtils {

    fun showErrorMessage(context: Context, view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        val params = snackBar.view.layoutParams as FrameLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        snackBar.view.layoutParams = params
        snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text).maxLines = 3
        snackBar.setBackgroundTint(ContextCompat.getColor(context, R.color.bottom_sheet_error_title))
        snackBar.show()
    }

    fun isEmailValid(email: String): Boolean {
        val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailPattern.toRegex())
    }

}