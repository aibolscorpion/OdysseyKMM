package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.Data
import timber.log.Timber

class TripsRepository(private val dao : Dao) {

    val trips : Flow<Data> = dao.getTrips()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertTrips(data: Data) {
        dao.insertTrips(data)
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
                insertTrips(response.body()!!)
            }
        }catch (e : Exception){
            Timber.e("exception - ${e.message}")
        }
    }

}