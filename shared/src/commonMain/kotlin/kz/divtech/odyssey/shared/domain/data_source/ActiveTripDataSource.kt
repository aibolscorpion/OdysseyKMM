package kz.divtech.odyssey.shared.domain.data_source

import androidx.paging.PagingSource
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

interface ActiveTripDataSource {
    fun getActiveTripsSortedByDate(statusType: List<String>, direction: List<String>): PagingSource<Int, Trip>
    fun getActiveTripsSortedByStatus(statusType: List<String>, direction: List<String>):  PagingSource<Int, Trip>
    fun insertActiveTrips(trips: List<Trip>)
    fun refreshActiveTrips(trips: List<Trip>)
    fun deleteActiveTrips()

}