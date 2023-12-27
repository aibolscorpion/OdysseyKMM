package kz.divtech.odyssey.shared.data.repository.pagingSource

import app.cash.paging.PagingSource
import app.cash.paging.PagingSourceLoadParams
import app.cash.paging.PagingSourceLoadResultError
import app.cash.paging.PagingSourceLoadResultPage
import app.cash.paging.PagingState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.isSuccess
import io.ktor.utils.io.errors.IOException
import kz.divtech.odyssey.shared.common.Constants
import kz.divtech.odyssey.shared.data.local.DataStoreManager
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.domain.model.trips.response.TripResponse
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip

class TripsPagingSource(private val httpClient: HttpClient,
                        private val dataStoreManager: DataStoreManager,
                        val isActive: Boolean,
                        val status: Array<String> = arrayOf(),
                        val direction: Array<String> = arrayOf(),
                        val sortBy: String = "date"):
    PagingSource<Int, Trip>(){
    override fun getRefreshKey(state: PagingState<Int, Trip>): Int?  = null
    override suspend fun load(params: PagingSourceLoadParams<Int>): LoadResult<Int, Trip> {
        val page = params.key ?: FIRST_PAGE_INDEX
        try {
            val httpResponse = httpClient.get{
                if(isActive){
                    url(HttpRoutes(dataStoreManager).getActiveTrips())
                }else{
                    url(HttpRoutes(dataStoreManager).getArchiveTrips())
                }
                parameter("page", page)
                parameter("status", status.joinToString(","))
                parameter("direction", direction.joinToString(","))
                parameter("sortBy", sortBy)
            }

            return when{
                httpResponse.status.isSuccess() -> {
                    val trips: TripResponse = httpResponse.body()
                    PagingSourceLoadResultPage(
                        data = trips.data,
                        prevKey = (page - 1).takeIf { it >= FIRST_PAGE_INDEX },
                        nextKey = if (trips.data.size >= Constants.TRIPS_PAGE_SIZE) page + 1 else null,
                    )
                }else -> {
                    PagingSourceLoadResultError(
                        Exception("Received a ${httpResponse.status}."),
                    )
                }
            }
        }catch (e: ClientRequestException) {
            return PagingSourceLoadResultError(
                Exception(e.response.status.description),
            )
        } catch (e: ServerResponseException) {
            return PagingSourceLoadResultError(
                Exception(e.response.status.description),
            )
        } catch (e: IOException) {
            return PagingSourceLoadResultError(
                Exception("${e.message}"),
            )
        } catch (e: Exception){
            return PagingSourceLoadResultError(
                Exception("${e.message}"),
            )
        }
    }

    companion object {
        const val FIRST_PAGE_INDEX = 1
    }

}