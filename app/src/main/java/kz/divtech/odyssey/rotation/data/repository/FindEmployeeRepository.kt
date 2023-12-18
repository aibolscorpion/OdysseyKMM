package kz.divtech.odyssey.rotation.data.repository

import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeResult
import kz.divtech.odyssey.rotation.data.local.SharedPrefsManager.saveUrl
import kz.divtech.odyssey.rotation.data.remote.retrofit.ProxyApiService

class FindEmployeeRepository(private val apiService: ProxyApiService) {
    suspend fun findByPhoneNumber(phoneNumber:String) : Result<EmployeeResult>{
        val response = apiService.getEmployeeByPhone(phoneNumber)
        if(response.isSuccess() && response.asSuccess().value.exists){
            val baseUrl = response.asSuccess().value.url
            saveUrl(baseUrl)
        }
        return response
    }

    suspend fun findByIIN(iin: String): Result<EmployeeResult>{
        val response = apiService.getEmployeeByIIN(iin)
        if(response.isSuccess() && response.asSuccess().value.exists){
            val baseUrl = response.asSuccess().value.url
            saveUrl(baseUrl)
        }
        return  response
    }

}