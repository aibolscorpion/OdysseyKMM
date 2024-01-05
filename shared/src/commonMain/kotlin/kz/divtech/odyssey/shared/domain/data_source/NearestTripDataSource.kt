package kz.divtech.odyssey.shared.domain.data_source

import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

interface NearestTripDataSource {
    suspend fun getNearestTrip(): Trip
    suspend fun insertNearestTrip(trip: Trip)
    suspend fun refreshNearesTrip(trip: Trip)
    suspend fun deleteNearestTrip()

}