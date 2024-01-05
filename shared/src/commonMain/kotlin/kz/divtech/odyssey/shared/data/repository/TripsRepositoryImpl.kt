package kz.divtech.odyssey.shared.data.repository

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url
import io.ktor.utils.io.errors.IOException
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.shared.common.Constants
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.data.repository.pagingSource.TripsPagingSource
import kz.divtech.odyssey.shared.domain.data_source.ActiveTripDataSource
import kz.divtech.odyssey.shared.domain.data_source.ArchiveTripsDataSource
import kz.divtech.odyssey.shared.domain.data_source.NearestTripDataSource
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.SingleTrip
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.shared.domain.repository.TripsRepository

class TripsRepositoryImpl(private val httpClient: HttpClient,
                          private val dataStoreManager: DataStoreManager,
                          private val activeTripsDataSource: ActiveTripDataSource,
                          private val archiveTripsDataSource: ArchiveTripsDataSource,
                          private val nearestTripDataSource: NearestTripDataSource
                          ): TripsRepository {
    override suspend fun getTripById(tripId: Int): Resource<SingleTrip> {
        return try {
            val result: SingleTrip = httpClient.get {
                url(HttpRoutes(dataStoreManager).getTripById(tripId))
            }.body()
            Resource.Success(data = result)
        }catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception){
            Resource.Error(message = "${e.message}")
        }
    }

    override suspend fun getNearestActiveTrip(): Resource<SingleTrip> {
        return try {
            val result: SingleTrip = httpClient.get {
                url(HttpRoutes(dataStoreManager).getNearestActiveTrip())
            }.body()
            nearestTripDataSource.refreshNearesTrip(result.data!!)
            Resource.Success(data = result)
        }catch (e: ClientRequestException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: ServerResponseException) {
            Resource.Error(message = e.response.status.description)
        } catch (e: IOException) {
            Resource.Error(message = "${e.message}")
        } catch (e: Exception){
            Resource.Error(message = "${e.message}")
        }
    }

    override fun getTripsSortedByDate(
        isActive: Boolean,
        statusType: Array<String>,
        direction: Array<String>
    ): Flow<PagingData<Trip>> {
        val pagingConfig = PagingConfig(pageSize = Constants.TRIPS_PAGE_SIZE,
            initialLoadSize = Constants.TRIPS_PAGE_SIZE * 3)
        return Pager(pagingConfig) {
            TripsPagingSource(httpClient, dataStoreManager,  isActive = isActive, statusType, direction)
        }.flow
    }

    override fun getTripsSortedByStatus(
        isActive: Boolean,
        statusType: Array<String>,
        direction: Array<String>
    ): Flow<PagingData<Trip>> {
        val pagingConfig = PagingConfig(pageSize = Constants.TRIPS_PAGE_SIZE,
            initialLoadSize = Constants.TRIPS_PAGE_SIZE * 3)
        return Pager(pagingConfig) {
            TripsPagingSource(httpClient, dataStoreManager, isActive = isActive, statusType, direction, sortBy = "status")
        }.flow
    }

    override suspend fun getActiveTripsSortedByDateFromDb(
        statusType: List<String>,
        direction: List<String>
    ): List<Trip> {
        return activeTripsDataSource.getActiveTripsSortedByDate(statusType, direction)
    }

    override suspend fun getActiveTripsSortedByStatusFromDb(
        statusType: List<String>,
        direction: List<String>
    ): List<Trip> {
        return activeTripsDataSource.getActiveTripsSortedByStatus(statusType, direction)
    }

    override suspend fun deleteActiveTrips() {
        activeTripsDataSource.deleteActiveTrips()
    }

    override suspend fun getArchiveTripsSortedByDateFromDb(
        statusType: List<String>,
        direction: List<String>
    ): List<Trip> {
        return archiveTripsDataSource.getArchiveTripsSortedByDate(statusType, direction)
    }

    override suspend fun getArchiveTripsSortedByStatusFromDb(
        statusType: List<String>,
        direction: List<String>
    ): List<Trip> {
        return archiveTripsDataSource.getArchiveTripsSortedByStatus(statusType, direction)
    }


    override suspend fun deleteArchiveTrips() {
        archiveTripsDataSource.deleteArchiveTrips()
    }

    override suspend fun getNearestTripFromBd(): Trip? {
        return nearestTripDataSource.getNearestTrip()
    }

    override suspend fun deleteNearestTrip() {
        nearestTripDataSource.deleteNearestTrip()
    }

}