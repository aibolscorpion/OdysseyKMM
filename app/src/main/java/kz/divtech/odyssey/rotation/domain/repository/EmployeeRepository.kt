package kz.divtech.odyssey.rotation.domain.repository

import androidx.annotation.WorkerThread
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants.ANDROID
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.DeviceInfo
import kz.divtech.odyssey.rotation.domain.model.login.update_phone.UpdatePhoneRequest
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import okhttp3.ResponseBody
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee

class EmployeeRepository(private val dao: Dao) {
    val employee = dao.observeEmployee()
    val uaConfirmed = dao.observeUAConfirmed()

    @WorkerThread
    suspend fun insertEmployee(employee: Employee){
        dao.insertEmployee(employee)
    }

    suspend fun updateEmployee(employee: Employee): Result<ResponseBody>{
        return RetrofitClient.getApiService().updateEmployee(employee)
    }

    @WorkerThread
    suspend fun deleteEmployee(){
        dao.deleteEmployee()
    }

    suspend fun getAndInstertEmployee(){
        val response = RetrofitClient.getApiService().getEmployeeInfo()
        if(response.isSuccess()){
            val employee = response.asSuccess().value.data
            insertEmployee(employee)
        }
    }

    suspend fun updatePhoneNumber(request: UpdatePhoneRequest): Result<ResponseBody>{
        return RetrofitClient.getApiService().updatePhoneNumberWithoutAuth(request)
    }

    suspend fun sendDeviceInfo(){
        val deviceType = android.os.Build.MANUFACTURER + android.os.Build.MODEL
        val deviceInfo = DeviceInfo(ANDROID, deviceType, SharedPrefs.fetchFirebaseToken(App.appContext))
        RetrofitClient.getApiService().sendDeviceInfo(deviceInfo)
    }

    suspend fun logoutFromServer(){
        RetrofitClient.getApiService().logout()
    }

}