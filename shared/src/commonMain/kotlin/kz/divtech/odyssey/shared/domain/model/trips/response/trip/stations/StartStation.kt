package kz.divtech.odyssey.shared.domain.model.trips.response.trip.stations

import kotlinx.serialization.Serializable


@Serializable
data class StartStation(
    val code: String?,
    val name: String?
)