package kz.divtech.odyssey.shared.domain.data_source

import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

interface ArchiveTripsDataSource {
    suspend fun getArchiveTripsSortedByDate(statusType: List<String>, direction: List<String>): List<Trip>
    suspend fun getArchiveTripsSortedByStatus(statusType: List<String>, direction: List<String>): List<Trip>
    suspend fun insertArchiveTrips(trips: List<Trip>)
    suspend fun refreshArchiveTrips(trips: List<Trip>)
    suspend fun deleteArchiveTrips()

}