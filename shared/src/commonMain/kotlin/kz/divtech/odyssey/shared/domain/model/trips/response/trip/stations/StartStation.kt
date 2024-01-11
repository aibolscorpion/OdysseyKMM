package kz.divtech.odyssey.shared.domain.model.trips.response.trip.stations

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable


@Serializable
@Parcelize
data class StartStation(
    val code: String?,
    val name: String?
): Parcelable