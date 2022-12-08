package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.domain.model.trips.Data

class ApplicationsRepository(private val dao : Dao) {

    val allApplications: Flow<Data> = dao.getAllApplications()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(data: Data) {
        dao.insertAllApplications(data)
    }
}