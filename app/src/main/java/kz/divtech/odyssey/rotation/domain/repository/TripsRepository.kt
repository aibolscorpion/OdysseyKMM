package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import timber.log.Timber

class TripsRepository(private val dao : Dao) {

    val trips : Flow<List<Trip>> = dao.getTrips()

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
    suspend fun getTripsFromServer(){
        try {
            val response = RetrofitClient.getApiService().getTrips()
            if(response.isSuccessful){
                insertTrips(response.body()?.data?.data!!)
            }
        }catch (e : Exception){
            Timber.e("exception - ${e.message}")
        }
    }

}