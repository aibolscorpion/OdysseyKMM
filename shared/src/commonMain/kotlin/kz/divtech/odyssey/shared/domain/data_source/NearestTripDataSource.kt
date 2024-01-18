package kz.divtech.odyssey.shared.domain.data_source

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

interface NearestTripDataSource {
    fun getNearestTrip(): Flow<Trip?>
    fun refreshNearesTrip(trip: Trip)
    fun deleteNearestTrip()

}