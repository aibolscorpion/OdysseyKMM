package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.trips.Data

class ApplicationsRepository(private val dao : Dao) {

    val data: Flow<Data> = dao.getData()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertData(data: Data) {
        dao.insertData(data)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteData(){
        dao.deleteData()
    }

    val employee: Flow<Employee> = dao.getEmployee()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertEmployee(employeeInfo: Employee){
        dao.insertEmployee(employeeInfo)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteEmployee(){
        dao.deleteEmployee()
    }



}