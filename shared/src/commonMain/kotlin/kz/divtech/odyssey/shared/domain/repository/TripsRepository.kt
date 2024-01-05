package kz.divtech.odyssey.shared.domain.repository

import app.cash.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.SingleTrip
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

interface TripsRepository {
    suspend fun getTripById(tripId: Int): Resource<SingleTrip>
    suspend fun getNearestActiveTrip(): Resource<SingleTrip>

    fun getTripsSortedByDate(isActive: Boolean, statusType: Array<String>, direction: Array<String>): Flow<PagingData<Trip>>

    fun getTripsSortedByStatus(isActive: Boolean, statusType: Array<String>, direction: Array<String>): Flow<PagingData<Trip>>
    suspend fun getActiveTripsSortedByDateFromDb(statusType: List<String>, direction: List<String>): List<Trip>
    suspend fun getActiveTripsSortedByStatusFromDb(statusType: List<String>, direction: List<String>): List<Trip>
    suspend fun deleteActiveTrips()
    suspend fun getArchiveTripsSortedByDateFromDb(statusType: List<String>, direction: List<String>): List<Trip>
    suspend fun getArchiveTripsSortedByStatusFromDb(statusType: List<String>, direction: List<String>):  List<Trip>
    suspend fun deleteArchiveTrips()

    suspend fun getNearestTripFromBd(): Trip?
    suspend fun deleteNearestTrip()
}