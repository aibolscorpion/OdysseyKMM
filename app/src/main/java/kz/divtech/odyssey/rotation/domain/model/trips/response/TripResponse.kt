package kz.divtech.odyssey.rotation.domain.model.trips.response

import androidx.room.PrimaryKey
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip

data class TripResponse(@PrimaryKey val data: List<Trip>,
                        val links: Links,
                        val meta: Meta
)