package kz.divtech.odyssey.rotation.domain.repository

import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeResult
import kz.divtech.odyssey.rotation.utils.SharedPrefs.clearUrl
import kz.divtech.odyssey.rotation.utils.SharedPrefs.saveUrl

class FindEmployeeRepository {
    suspend fun findByPhoneNumber(phoneNumber:String) : Result<EmployeeResult>{
        App.appContext.clearUrl()
        val response = RetrofitClient.getApiService().getEmployeeByPhone(phoneNumber)
        if(response.isSuccess() && response.asSuccess().value.exists){
            val baseUrl = response.asSuccess().value.url
            App.appContext.saveUrl(baseUrl)
        }
        return response
    }

    suspend fun findByIIN(iin: String): Result<EmployeeResult>{
        App.appContext.clearUrl()
        val response = RetrofitClient.getApiService().getEmployeeByIIN(iin)
        if(response.isSuccess() && response.asSuccess().value.exists){
            val baseUrl = response.asSuccess().value.url
            App.appContext.saveUrl(baseUrl)
        }
        return  response
    }

}