package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kz.divtech.odyssey.rotation.data.local.Dao
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants.ANDROID
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.DeviceInfo
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import timber.log.Timber

class EmployeeRepository(private val dao: Dao) {

    val employee: Flow<Employee> = dao.getEmployee()
    private var firstTimeEmployee = true
    private var firstTimeDeviceInfo = true

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
            if(firstTimeEmployee){
                val response = RetrofitClient.getApiService().getEmployeeInfo()
                if(response.isSuccessful){
                    insertEmployee(response.body()!!)
                    firstTimeEmployee = false
                }
            }
        }catch (e: Exception){
            Timber.e("exception - ${e.message}")
        }
    }


    suspend fun sendDeviceInfo(){
            val deviceType = android.os.Build.MANUFACTURER + android.os.Build.MODEL
            val deviceInfo = DeviceInfo(ANDROID, deviceType, SharedPrefs.fetchFirebaseToken(App.appContext))
        try{
            if(firstTimeDeviceInfo){
                val response = RetrofitClient.getApiService().sendDeviceInfo(deviceInfo)
                if(response.isSuccessful){
                    firstTimeDeviceInfo = false
                }
            }
        }catch (e: Exception){
            Timber.i("exception - ${e.message}")
        }
    }

    suspend fun logoutFromServer(){
        try{
            RetrofitClient.getApiService().logout()
        }catch (e: Exception){
            Timber.e("exception $e")
        }
    }

}