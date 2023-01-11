package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kz.divtech.odyssey.rotation.data.local.Dao
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import timber.log.Timber

class EmployeeRepository(private val dao: Dao) {

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

    suspend fun getEmployeeFromServer(){
        try{
            val response = RetrofitClient.getApiService().getEmployeeInfo()
            if(response.isSuccessful){
                insertEmployee(response.body()!!)
            }
        }catch (e: Exception){
            Timber.e("exception - ${e.message}")
        }
    }


}