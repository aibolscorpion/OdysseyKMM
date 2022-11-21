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
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.trips.ApplicationStatus

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

    fun getStatusByApplication(trip: Trip): ApplicationStatus {
        when(trip.status){

            Constants.STATUS_OPENED ->{
                if(trip.segments == null) {
                    return ApplicationStatus.OPENED_WITHOUT_DETAILS
                }else{
                    val list = ArrayList<String>()
                    trip.segments.forEachIndexed{ _, segment ->
                        if(segment.status.equals(Constants.STATUS_OPENED)){
                            if(segment.active_process.equals(Constants.WATCHING)){
                                list.add(Constants.APP_STATUS_ON_THE_WAITING_LIST)
                            }else if(segment.active_process == null){
                                list.add(Constants.STATUS_OPENED)
                            }
                        }

                        if(list.contains(Constants.STATUS_OPENED) && list.contains(Constants.APP_STATUS_ON_THE_WAITING_LIST)){
                            return ApplicationStatus.OPENED_WITH_DETAILS_AND_OPENED_ON_THE_WAITING_LIST
                        }else if(list.contains(Constants.STATUS_OPENED)){
                            return ApplicationStatus.OPENED_WITH_DETAILS
                        }else if(list.contains(Constants.APP_STATUS_ON_THE_WAITING_LIST)){
                            return ApplicationStatus.OPENED_ON_THE_WAITING_LIST
                        }
                    }
                }
            }

            Constants.APP_STATUS_PARTLY -> {
                val list = ArrayList<String>()
                trip.segments?.forEachIndexed{ _, segment ->
                    if(segment.status.equals(Constants.APP_STATUS_ISSUED)){
                        list.add(Constants.APP_STATUS_ISSUED)
                    }else if(segment.status.equals(Constants.STATUS_OPENED) && segment.active_process.equals(Constants.WATCHING)){
                        list.add(Constants.APP_STATUS_ON_THE_WAITING_LIST)
                    }else if(segment.status.equals(Constants.STATUS_OPENED)){
                        list.add(Constants.STATUS_OPENED)
                    }
                }
                if(list.contains(Constants.APP_STATUS_ISSUED) && list.contains(Constants.STATUS_OPENED)){
                    return ApplicationStatus.PARTLY_ISSUED_AND_OPENED
                }else if(list.contains(Constants.APP_STATUS_ISSUED) && list.contains(Constants.APP_STATUS_ON_THE_WAITING_LIST)){
                    return ApplicationStatus.PARTLY_ISSUED_AND_OPENED_ON_THE_WAITING_LIST
                }
            }

            Constants.APP_STATUS_RETURNED -> {
                trip.segments?.forEachIndexed { index, segment ->
                    if (segment.status == Constants.APP_STATUS_ISSUED) {
                        return ApplicationStatus.RETURNED_PARTLY
                    } else if (index == trip.segments.size - 1) {
                        return ApplicationStatus.RETURNED_FULLY
                    }
                }
            }

            Constants.APP_STATUS_ISSUED -> return ApplicationStatus.ISSUED
        }
        return ApplicationStatus.OPENED_WITHOUT_DETAILS
    }
}