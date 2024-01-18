package kz.divtech.odyssey.shared.data.repository.remotemediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.data_source.ActiveTripDataSource
import kz.divtech.odyssey.shared.domain.data_source.ArchiveTripsDataSource
import kz.divtech.odyssey.shared.domain.model.trips.response.TripResponse
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip
import java.io.IOException

@ExperimentalPagingApi
class TripRemoteMediator(private val httpClient: HttpClient,
                         private val dataStoreManager: DataStoreManager,
                         private val isActive: Boolean,
                         val status: Array<String> = arrayOf(),
                         val direction: Array<String> = arrayOf(),
                         private val sortBy: String = "date",
                         private val activeTripDataSource: ActiveTripDataSource,
                         private val archiveTripDataSource: ArchiveTripsDataSource) : RemoteMediator<Int, Trip>() {
    private var pageIndex = 0
    override suspend fun load(loadType: LoadType, state: PagingState<Int, Trip>): MediatorResult {

        pageIndex = getPageIndex(loadType) ?:
        return MediatorResult.Success(endOfPaginationReached = true)

        return try {
            val httpResponse = httpClient.get{
                if(isActive){
                    url(HttpRoutes(dataStoreManager).getActiveTrips())
                }else{
                    url(HttpRoutes(dataStoreManager).getArchiveTrips())
                }
                parameter("page", pageIndex)
                parameter("status", status.joinToString(","))
                parameter("direction", direction.joinToString(","))
                parameter("sortBy", sortBy)
            }
            val trips: TripResponse = httpResponse.body()
            when(loadType){
                LoadType.REFRESH -> {
                    if(isActive){
                        activeTripDataSource.refreshActiveTrips(trips.data)
                    }else{
                        archiveTripDataSource.refreshArchiveTrips(trips.data)
                    }
                }
                else -> {
                    if(isActive){
                        activeTripDataSource.insertActiveTrips(trips.data)
                    }else{
                        archiveTripDataSource.insertArchiveTrips(trips.data)
                    }
                }
            }
            MediatorResult.Success(trips.data.size < state.config.pageSize)
        }catch (e: ClientRequestException) {
            return MediatorResult.Error(
                Exception(e.response.status.description),
            )
        }catch (e: ServerResponseException) {
            return MediatorResult.Error(
                Exception(e.response.status.description),
            )
        }catch (e: IOException) {
            return MediatorResult.Error(
                Exception("${e.message}"),
            )
        }catch (e: Exception){
            return MediatorResult.Error(
                Exception("${e.message}"),
            )
        }
    }

    private fun getPageIndex(loadType: LoadType): Int?{
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