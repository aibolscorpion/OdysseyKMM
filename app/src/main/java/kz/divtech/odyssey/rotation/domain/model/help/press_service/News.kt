package kz.divtech.odyssey.rotation.domain.model.help.press_service

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class News(val title: String, val content: String, val time: String) : Parcelable