package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.trips.Data

class ApplicationsRepository(private val dao : Dao) {

    val allApplications: Flow<Data> = dao.getAllApplications()

    val employeeInfo: Flow<Employee> = dao.getEmployeeInfo()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertApplications(data: Data) {
        dao.insertAllApplications(data)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertEmployee(employeeInfo: Employee){
        dao.insertEmployeeInfo(employeeInfo)
    }
}