package kz.divtech.odyssey.rotation.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kz.divtech.odyssey.rotation.R
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Utils {
    private const val SERVER_PATTERN = "yyyy-MM-dd HH:mm:ss"
    const val DEFAULT_PATTERN = "d MMM yyyy, HH:mm"
    const val DAY_MONTH_DAY_OF_WEEK_PATTERN = "d MMM EE"
    const val DAY_MONTH_PATTERN = "d MMM"
    const val HOUR_MINUTE_PATTERN = "HH:mm"

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

    fun formatByGivenPattern(dateTime: String?, pattern: String): String{
        val serverDateTimeFormat = DateTimeFormatter.ofPattern(SERVER_PATTERN)
        val parsedDateTime = LocalDateTime.parse(dateTime, serverDateTimeFormat)

        val format = DateTimeFormatter.ofPattern(pattern)
        return parsedDateTime.format(format)
    }

    fun getLocalDateTimeByPattern(serverDateTime: String): LocalDateTime{
        val serverDateTimeFormat = DateTimeFormatter.ofPattern(SERVER_PATTERN)
        return LocalDateTime.parse(serverDateTime, serverDateTimeFormat)
    }

    fun getLocalDateByPattern(serverDateTime: String): LocalDate{
        val serverDateTimeFormat = DateTimeFormatter.ofPattern(SERVER_PATTERN)
        return LocalDate.parse(serverDateTime, serverDateTimeFormat)
    }

    fun RecyclerView.addItemDecorationWithoutLastDivider() {

        if (layoutManager !is LinearLayoutManager)
            return

        addItemDecoration(object : RecyclerView.ItemDecoration() {
            private val mDivider: Drawable = ContextCompat.getDrawable(context, R.drawable.divider)!!
            override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
                val dividerLeft = parent.paddingLeft
                val dividerRight = parent.width - parent.paddingRight
                val childCount = parent.childCount
                for (i in 0..childCount - 2) {
                    val child = parent.getChildAt(i)
                    val params = child.layoutParams as RecyclerView.LayoutParams
                    val dividerTop = child.bottom + params.bottomMargin
                    val dividerBottom = dividerTop + mDivider.intrinsicHeight
                    mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                    mDivider.draw(canvas)
                }
            }
        })
    }
}