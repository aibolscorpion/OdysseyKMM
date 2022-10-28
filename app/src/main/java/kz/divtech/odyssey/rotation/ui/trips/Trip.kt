package kz.divtech.odyssey.rotation.ui.trips

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trip(val title: String, val description: String, val isTicketPurchased: Boolean = true) : Parcelable
