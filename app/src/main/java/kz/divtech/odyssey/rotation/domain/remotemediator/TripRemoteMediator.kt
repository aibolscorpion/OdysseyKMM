package kz.divtech.odyssey.rotation.domain.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository

@ExperimentalPagingApi
class TripRemoteMediator(val dao: Dao, private val orderDir: TripsRepository.OrderDir) : RemoteMediator<Int, Trip>() {

    var pageIndex = 0

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Trip>): MediatorResult {
        pageIndex = getPageIndex(loadType)?:
            return MediatorResult.Success(true)

        return try{
            val response = RetrofitClient.getApiService().getTrips(pageIndex,  orderDir = orderDir.value)
            val trips = response.body()?.data?.data!!
            when(loadType){
                LoadType.REFRESH -> dao.refreshTrips(trips)
                else -> dao.insertTrips(trips)
            }

            MediatorResult.Success(trips.size < state.config.pageSize)

        }catch (e: Exception){
            MediatorResult.Error(e)
        }
    }

    private fun getPageIndex(loadType: LoadType) : Int?{
        return when(loadType){
            LoadType.REFRESH -> 1
            LoadType.PREPEND -> null
            LoadType.APPEND -> ++pageIndex
        }
    }

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.SKIP_INITIAL_REFRESH
    }
}
