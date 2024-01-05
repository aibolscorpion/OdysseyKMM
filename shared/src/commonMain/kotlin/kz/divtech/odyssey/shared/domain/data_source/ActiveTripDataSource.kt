package kz.divtech.odyssey.shared.domain.data_source

import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

interface ActiveTripDataSource {
    suspend fun getActiveTripsSortedByDate(statusType: List<String>, direction: List<String>): List<Trip>
    suspend fun getActiveTripsSortedByStatus(statusType: List<String>, direction: List<String>):  List<Trip>
    suspend fun insertActiveTrips(trips: List<Trip>)
    suspend fun refreshActiveTrips(trips: List<Trip>)
    suspend fun deleteActiveTrips()

}