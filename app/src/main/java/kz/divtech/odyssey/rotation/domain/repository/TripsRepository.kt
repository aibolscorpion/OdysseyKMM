package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.Constants.TRIPS_PAGE_SIZE
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.remotemediator.TripRemoteMediator
import timber.log.Timber


class TripsRepository(private val dao : Dao) {

    val nearestActiveTrip: Flow<Trip> = dao.observeNearestActiveTrip()
    fun getTripById(id: Int): Flow<Trip> = dao.observeTripById(id)
    private var firstTime = true

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteTrips(){
        dao.deleteTrips()
    }


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun refreshTrips(data: List<Trip>){
        dao.refreshTrips(data)
    }

    suspend fun getTripsFromFirstPage(){
        try {
            if(firstTime){
                val response = RetrofitClient.getApiService().getTrips(1, orderDir = OrderDir.DESC.value)
                if(response.isSuccessful){
                    val trips = response.body()?.data?.data!!
                    refreshTrips(trips)
                    firstTime = false
                }
            }
        }catch (e: Exception){
            Timber.i("exception - $e")
        }

    }

    @OptIn(ExperimentalPagingApi::class)
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getActivePagingTrip(): Flow<PagingData<Trip>>{

       return Pager(
           config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
           remoteMediator = TripRemoteMediator(dao, OrderDir.ASC, isActive = true),
           pagingSourceFactory = {dao.getActiveTrips()}
       ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getArchivePagingTrip(): Flow<PagingData<Trip>>{

        return Pager(
            config = PagingConfig(pageSize = TRIPS_PAGE_SIZE),
            remoteMediator = TripRemoteMediator(dao, OrderDir.DESC, isActive = false),
            pagingSourceFactory = {dao.getArchiveTrips()}
        ).flow
    }

    enum class OrderDir(val value: String){
        DESC("desc"), ASC("asc")
    }

}