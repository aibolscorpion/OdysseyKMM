package kz.divtech.odyssey.rotation.data.repository

import androidx.annotation.WorkerThread
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.common.Constants.ANDROID
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.DeviceInfo
import kz.divtech.odyssey.rotation.domain.model.login.update_phone.UpdatePhoneRequest
import okhttp3.ResponseBody
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.fetchFirebaseToken
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService

class EmployeeRepository(private val dao: Dao, private val apiService: ApiService) {
    val employee = dao.observeEmployee()
    val uaConfirmed = dao.observeUAConfirmed()

    @WorkerThread
    suspend fun insertEmployee(employee: Employee){
        dao.insertEmployee(employee)
    }

    suspend fun updateEmployee(employee: Employee): Result<ResponseBody>{
        return apiService.updateEmployee(employee)
    }

    @WorkerThread
    suspend fun deleteEmployee(){
        dao.deleteEmployee()
    }

    suspend fun getAndInstertEmployee(){
        val response = apiService.getEmployeeInfo()
        if(response.isSuccess()){
            val employee = response.asSuccess().value.data
            insertEmployee(employee)
        }
    }

    suspend fun updatePhoneNumber(request: UpdatePhoneRequest): Result<ResponseBody>{
        return apiService.updatePhoneNumberWithoutAuth(request)
    }

    suspend fun sendDeviceInfo(){
        val deviceType = android.os.Build.MANUFACTURER + android.os.Build.MODEL
        val deviceInfo = DeviceInfo(ANDROID, deviceType, App.appContext.fetchFirebaseToken())
        apiService.sendDeviceInfo(deviceInfo)
    }

    suspend fun logoutFromServer(){
        apiService.logout()
    }

}