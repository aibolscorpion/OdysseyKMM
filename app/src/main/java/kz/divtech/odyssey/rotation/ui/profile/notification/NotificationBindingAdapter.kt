package kz.divtech.odyssey.rotation.ui.profile.notification

import android.widget.TextView
import androidx.databinding.BindingAdapter

object NotificationBindingAdapter {

    @BindingAdapter("updatedAt")
    @JvmStatic fun setUpdatedAt(textView: TextView, updatedAt: String?){
        textView.text = updatedAt
    }
}