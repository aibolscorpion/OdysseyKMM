package kz.divtech.odyssey.rotation.data.repository

import androidx.annotation.WorkerThread
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.data.local.Dao
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.DeviceInfo
import kz.divtech.odyssey.rotation.domain.model.login.update_phone.UpdatePhoneRequest
import okhttp3.ResponseBody
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService
import kz.divtech.odyssey.rotation.domain.model.login.login.AuthRequest
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Document
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeRequest
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse

class ProfileRepository(private val dao: Dao, private val apiService: ApiService) {
    val employee = dao.observeProfile()
    val uaConfirmed = dao.observeUAConfirmed()

    @WorkerThread
    suspend fun insertProfile(employee: Employee){
        dao.insertProfile(employee)
    }

    suspend fun updateProfile(employee: Employee): Result<ResponseBody>{
        return apiService.updateProfile(employee)
    }

    @WorkerThread
    suspend fun deleteProfile(){
        dao.deleteProfile()
    }

    suspend fun getAndInsertProfile(){
        val response = apiService.getProfile()
        if(response.isSuccess()){
            val employee = response.asSuccess().value.data
            insertProfile(employee)
        }
    }

    suspend fun updateDocument(document: Document): Result<ResponseBody>{
        val response = apiService.updateDocument(document)
        if(response.isSuccess()){
            getAndInsertProfile()
        }
        return response
    }

    suspend fun sendDeviceInfo(deviceInfo: DeviceInfo){
        apiService.sendDeviceInfo(deviceInfo)
    }


    suspend fun updatePhoneNumberWihoutAuth(request: UpdatePhoneRequest): Result<ResponseBody> {
        return apiService.updatePhoneNumberWithoutAuth(request)
    }

    suspend fun updatePhoneWithAuth(phoneNumber: String): Result<CodeResponse>{
        val codeRequest = CodeRequest(phoneNumber, Config.IS_TEST)
        return apiService.updatePhoneNumberWithAuth(codeRequest)
    }

    suspend fun updatePhoneConfirm(authRequest : AuthRequest): Result<ResponseBody>{
        val response = apiService.updatePhoneConfirm(authRequest)
        if(response.isSuccess()) {
            getAndInsertProfile()
        }
        return response
    }
}