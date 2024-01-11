package kz.divtech.odyssey.shared.domain.data_source

import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

interface NearestTripDataSource {
    suspend fun getNearestTrip(): Flow<Trip?>
    suspend fun refreshNearesTrip(trip: Trip)
    suspend fun deleteNearestTrip()

}