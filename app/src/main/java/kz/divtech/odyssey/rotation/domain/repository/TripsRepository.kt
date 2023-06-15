package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.Constants.ALL_DIRECTION
import kz.divtech.odyssey.rotation.app.Constants.TRIPS_PAGE_SIZE
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.SingleTrip
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.rotation.domain.remotemediator.TripRemoteMediator

class TripsRepository(private val dao : Dao) {
    val nearestActiveTrip = dao.getNearestActiveTrip()
    suspend fun getTripById(id: Int): Result<SingleTrip> {
        return RetrofitClient.getApiService().getTripById(id)
    }

    @WorkerThread
    suspend fun deleteAllTrips(){
        dao.deleteActiveTrips()
        dao.deleteArchiveTrips()
    }

    @WorkerThread
    suspend fun deleteNearestActiveTrip(){
        dao.deleteNearestActiveTrip()
    }

    suspend fun getNearestActiveTrip(){
        val result = RetrofitClient.getApiService().getNearestActiveTrip()
        if(result.isSuccess()){
            result.asSuccess().value.data?.let {
                dao.refreshNearestActiveTrip(result.asSuccess().value)
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getTripsSortedByDate(isActive: Boolean): Flow<PagingData<Trip>>{
        return if(isActive){
            Pager(
                config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                remoteMediator = TripRemoteMediator(dao, isActive = true),
                pagingSourceFactory = {dao.getActiveTripsSortedByDate()}
            ).flow
        }else{
            Pager(
                config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                remoteMediator = TripRemoteMediator(dao, isActive = false),
                pagingSourceFactory = {dao.getArchiveTripsSortedByDate()}
            ).flow
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getTripsSortedByStatus(isActive: Boolean): Flow<PagingData<Trip>>{
        return if(isActive){
            Pager(
                config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                remoteMediator = TripRemoteMediator(dao, isActive = true),
                pagingSourceFactory = {dao.getActiveTripsSortedByStatus()}
            ).flow
        }else{
            Pager(
                config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                remoteMediator = TripRemoteMediator(dao, isActive = false),
                pagingSourceFactory = {dao.getArchiveTripsSortedByStatus()}
            ).flow
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getFilteredTrips(isActive: Boolean, statusType: Array<String>, direction: String)
                                : Flow<PagingData<Trip>>{
        return if(isActive){
            if(direction == ALL_DIRECTION){
                Pager(
                    config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                    remoteMediator = TripRemoteMediator(dao, isActive = true, statusType),
                    pagingSourceFactory = {dao.getFilteredActiveTripsWithAllAllDirection(statusType)}
                ).flow
            }else{
                Pager(
                    config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                    remoteMediator = TripRemoteMediator(dao, isActive = true, statusType, direction),
                    pagingSourceFactory = {dao.getFilteredActiveTrips(statusType, direction)}
                ).flow
            }
        }else{
            if(direction == ALL_DIRECTION){
                Pager(
                    config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                    remoteMediator = TripRemoteMediator(dao, isActive = false, statusType),
                    pagingSourceFactory = {dao.getFilteredArchiveTripsWithAllAllDirection(statusType)}
                ).flow
            }else{
                Pager(
                    config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                    remoteMediator = TripRemoteMediator(dao, isActive = false, statusType, direction),
                    pagingSourceFactory = {dao.getFilteredArchiveTrips(statusType, direction)}
                ).flow
            }

        }
    }

}