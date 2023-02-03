package kz.divtech.odyssey.rotation.domain.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository
import kz.divtech.odyssey.rotation.utils.LocalDateTimeUtils
import java.time.LocalDate

@ExperimentalPagingApi
class TripRemoteMediator(val dao: Dao, private val orderDir: TripsRepository.OrderDir, val isActive: Boolean) : RemoteMediator<Int, Trip>() {
    private var pageIndex = 0

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Trip>): MediatorResult {
        pageIndex = getPageIndex(loadType)?:
            return MediatorResult.Success(true)

        return try{
            val response = RetrofitClient.getApiService().getTrips(pageIndex,  orderDir = orderDir.value)
            val trips = response.body()?.data?.data!!
            val newTrips = mutableListOf<Trip>()
            val today = LocalDate.now()

            trips.forEach{
                val localDate = LocalDateTimeUtils.getLocalDateByPattern(it.date!!)
                if(isActive){
                    if(localDate.isAfter(today)){
                        newTrips.add(it)
                    }
                }else{
                    if(localDate.isBefore(today)){
                        newTrips.add(it)
                    }
                }

            }
            when(loadType){
                LoadType.REFRESH -> dao.refreshTrips(newTrips)
                else -> dao.insertTrips(newTrips)
            }
            MediatorResult.Success(newTrips.size < state.config.pageSize)
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