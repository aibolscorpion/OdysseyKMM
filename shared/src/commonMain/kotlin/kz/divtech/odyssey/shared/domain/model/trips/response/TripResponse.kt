package kz.divtech.odyssey.shared.domain.model.trips.response

import kotlinx.serialization.Serializable
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Links
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Meta
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip


@Serializable
data class TripResponse(val data: List<Trip>,
                        val links: Links,
                        val meta: Meta
)