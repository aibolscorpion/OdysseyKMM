package kz.divtech.odyssey.shared.domain.data_source

import androidx.paging.PagingSource
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

interface ArchiveTripsDataSource {
    fun getArchiveTripsSortedByDate(statusType: List<String>, direction: List<String>): PagingSource<Int, Trip>
    fun getArchiveTripsSortedByStatus(statusType: List<String>, direction: List<String>): PagingSource<Int, Trip>
    fun insertArchiveTrips(trips: List<Trip>)
    fun refreshArchiveTrips(trips: List<Trip>)
    fun deleteArchiveTrips()

}