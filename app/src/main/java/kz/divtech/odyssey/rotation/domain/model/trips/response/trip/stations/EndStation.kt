package kz.divtech.odyssey.rotation.domain.model.trips.response.trip.stations

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class EndStation(
    val code: String,
    val name: String
): Parcelable