package kz.divtech.odyssey.rotation.data.repository

import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeResult
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.saveUrl
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService

class FindEmployeeRepository(private val apiService: ApiService) {
    suspend fun findByPhoneNumber(phoneNumber:String) : Result<EmployeeResult>{
        val response = apiService.getEmployeeByPhone(phoneNumber)
        if(response.isSuccess() && response.asSuccess().value.exists){
            val baseUrl = response.asSuccess().value.url
            App.appContext.saveUrl(baseUrl)
        }
        return response
    }

    suspend fun findByIIN(iin: String): Result<EmployeeResult>{
        val response = apiService.getEmployeeByIIN(iin)
        if(response.isSuccess() && response.asSuccess().value.exists){
            val baseUrl = response.asSuccess().value.url
            App.appContext.saveUrl(baseUrl)
        }
        return  response
    }

}