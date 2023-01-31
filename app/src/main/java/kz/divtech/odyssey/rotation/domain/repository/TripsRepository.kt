package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.TripRemoteMediator


class TripsRepository(private val dao : Dao) {

    fun getTripById(id: Int): Flow<Trip> = dao.getTripById(id)


    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertTrips(trips: List<Trip>) {
        dao.insertTrips(trips)
    }

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

    @OptIn(ExperimentalPagingApi::class)
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getActivePagingTrip(): Flow<PagingData<Trip>>{

       return Pager(
           config = PagingConfig(pageSize = 20, enablePlaceholders = false),
           remoteMediator = TripRemoteMediator(dao),
           pagingSourceFactory = {dao.getActiveTrips()}
       ).flow
    }

    @OptIn(ExperimentalPagingApi::class)
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun getArchivePagingTrip(): Flow<PagingData<Trip>>{

        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            remoteMediator = TripRemoteMediator(dao),
            pagingSourceFactory = {dao.getArchiveTrips()}
        ).flow
    }

}