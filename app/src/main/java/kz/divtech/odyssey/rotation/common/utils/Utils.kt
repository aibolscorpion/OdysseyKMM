package kz.divtech.odyssey.rotation.common.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.domain.model.profile.Country
import kz.divtech.odyssey.rotation.domain.model.profile.CountryList
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.PushNotification
import kz.divtech.odyssey.rotation.domain.model.trips.refund.applications.RefundAppItem
import kz.divtech.odyssey.rotation.ui.MainActivity
import kz.divtech.odyssey.rotation.data.local.SharedPrefsManager.fetchAppLanguage
import java.io.File
import java.util.Locale

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
        val rawId = when(fetchAppLanguage()){
            else -> R.raw.countries
        }
        val jsonString = App.appContext.resources.openRawResource(rawId).bufferedReader().
            use { it.readText() }
        return Gson().fromJson(jsonString, CountryList::class.java).countries
    }

    fun Fragment.showBottomNavigation(){
        (activity as MainActivity).binding.bottomNavigationView.isVisible = true
    }
    fun Fragment.hideBottomNavigation(){
        (activity as MainActivity).binding.bottomNavigationView.isVisible = false
    }

    fun Fragment.showToolbar(){
        (activity as MainActivity).binding.mainToolbar.isVisible = true
    }
    fun Fragment.hideToolbar(){
        (activity as MainActivity).binding.mainToolbar.isVisible = false
    }

    fun Fragment.setMainActivityBackgroundColor(@ColorRes color: Int){
        (activity as MainActivity).binding.mainActivityCL.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
    }

    fun Fragment.changeStatusBarColor(@ColorRes statusBarColor: Int){
        val window: Window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(requireContext(), statusBarColor)
    }

    fun Context.getAppLocale(): Locale = this.resources.configuration.locales[0]

    fun Context.changeAppLocale(languageCode: String): Context {
        val configuration: Configuration = this.resources.configuration
        val newLocale = Locale(languageCode)
        configuration.setLocale(newLocale)
        return this.createConfigurationContext(configuration)
    }

}