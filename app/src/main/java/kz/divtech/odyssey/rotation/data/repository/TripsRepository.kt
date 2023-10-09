package kz.divtech.odyssey.rotation.data.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.Constants.TRIPS_PAGE_SIZE
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.SingleTrip
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.rotation.data.remotemediator.TripRemoteMediator

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
            if(result.asSuccess().value.data != null){
                dao.refreshNearestActiveTrip(result.asSuccess().value)
            }else{
                dao.deleteNearestActiveTrip()
            }
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getTripsSortedByDate(isActive: Boolean, statusType: Array<String>, direction: Array<String>):
            Flow<PagingData<Trip>>{
        return if(isActive){
            Pager(
                config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                remoteMediator = TripRemoteMediator(dao, isActive = true),
                pagingSourceFactory = {dao.getActiveTripsSortedByDate(statusType, direction)}
            ).flow
        }else{
            Pager(
                config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                remoteMediator = TripRemoteMediator(dao, isActive = false),
                pagingSourceFactory = {dao.getArchiveTripsSortedByDate(statusType, direction)}
            ).flow
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getTripsSortedByStatus(isActive: Boolean, statusType: Array<String>, direction: Array<String>):
            Flow<PagingData<Trip>>{
        return if(isActive){
            Pager(
                config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                remoteMediator = TripRemoteMediator(dao, isActive = true, sortBy = "status"),
                pagingSourceFactory = {dao.getActiveTripsSortedByStatus(statusType, direction)}
            ).flow
        }else{
            Pager(
                config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
                remoteMediator = TripRemoteMediator(dao, isActive = false, sortBy = "status"),
                pagingSourceFactory = {dao.getArchiveTripsSortedByStatus(statusType, direction)}
            ).flow
        }
    }

}