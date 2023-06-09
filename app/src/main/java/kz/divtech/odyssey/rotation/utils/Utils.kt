package kz.divtech.odyssey.rotation.utils

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.domain.model.profile.Country
import kz.divtech.odyssey.rotation.domain.model.profile.CountryList
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.PushNotification
import kz.divtech.odyssey.rotation.domain.model.trips.refund.applications.RefundAppItem
import kz.divtech.odyssey.rotation.ui.MainActivity
import java.io.File

object Utils {

    fun Bundle.convertToNotification(): PushNotification {
        val id = getString(Constants.NOTIFICATION_DATA_ID)
        val sendTime = getString(Constants.NOTIFICATION_DATA_CREATED_AT)
        val notifiableType = getString(Constants.NOTIFICATION_DATA_NOTIFICATION_TYPE)
        val title = getString(Constants.NOTIFICATION_DATA_TITLE)
        val content = getString(Constants.NOTIFICATION_DATA_CONTENT)
        val type = getString(Constants.NOTIFICATION_DATA_TYPE)
        val isImportant = getString(Constants.NOTIFICATION_DATA_IS_IMPORTANT)?.toBoolean()
        val applicationId = getString(Constants.NOTIFICATION_DATA_APPLICATION_ID)?.toInt()
        return PushNotification(id!!, notifiableType!!, sendTime!!,
            title!!, content!!, type!!, isImportant!!, applicationId)
    }

    fun getRefundSegmentStatus(refundAppList: List<RefundAppItem>, segmentId: Int): String?{
        if(refundAppList.isNotEmpty()){
            refundAppList.forEach { refundAppItem ->
                refundAppItem.segments.forEach { refundSegment ->
                    if(refundSegment.segment_id == segmentId){
                        return when(refundAppItem.status){
                            Constants.REFUND_STATUS_PENDING -> Constants.REFUND_STATUS_PENDING
                            Constants.REFUND_STATUS_REJECTED -> Constants.REFUND_STATUS_REJECTED
                            else -> refundSegment.status
                        }
                    }
                }
            }
        }
        return null
    }

    fun File.getFileSize(): String {
        val size: Long = this.length() / 1024 // Get size and convert bytes into Kb.
        return if (size >= 1024) {
            (size / 1024).toString() + " Mb"
        } else {
            "$size Kb"
        }
    }

    fun getCountryList(): List<Country>{
        val jsonString = App.appContext.resources.openRawResource(R.raw.countries).bufferedReader().
            use { it.readText() }
        return Gson().fromJson(jsonString, CountryList::class.java).countries
    }

    fun Fragment.hideBottomNavigationAndStatusBar(){
        (activity as MainActivity).binding.apply {
            mainToolbar.isVisible = false
            bottomNavigationView.isVisible = false
        }
    }

    fun Fragment.showBottomNavigationAndStatusBar(){
        (activity as MainActivity).binding.apply {
            mainToolbar.isVisible = true
            bottomNavigationView.isVisible = true
        }
    }

    fun Fragment.changeStatusBarColor(@ColorRes statusBarColor: Int){
        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireActivity(), statusBarColor)
    }

}