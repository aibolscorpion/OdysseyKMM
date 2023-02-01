package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kz.divtech.odyssey.rotation.data.local.Dao
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import timber.log.Timber

class EmployeeRepository(private val dao: Dao) {

    val employee: Flow<Employee> = dao.getEmployee()
    private var firstTime = true

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

    suspend fun logoutFromServer(){
        try{
            RetrofitClient.getApiService().logout()
        }catch (e: Exception){
            Timber.e("exception $e")
        }
    }

    suspend fun getEmployeeFromServer(){
        try{
            if(firstTime){
                firstTime = false
                val response = RetrofitClient.getApiService().getEmployeeInfo()
                if(response.isSuccessful){
                    insertEmployee(response.body()!!)
                }
            }
        }catch (e: Exception){
            Timber.e("exception - ${e.message}")
        }
    }


}