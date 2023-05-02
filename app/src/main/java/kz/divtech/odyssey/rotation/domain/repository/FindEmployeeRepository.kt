package kz.divtech.odyssey.rotation.domain.repository

import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeResult

class FindEmployeeRepository {

    suspend fun findByPhoneNumber(phoneNumber:String) : Result<EmployeeResult>{
        SharedPrefs.clearUrl(App.appContext)
        val response = RetrofitClient.getApiService().getEmployeeByPhone(phoneNumber)
        if(response.isSuccess()){
            val baseUrl = response.asSuccess().headers!![Config.BASE_URL_KEY]!!
            SharedPrefs.saveUrl(baseUrl, App.appContext)
        }
        return response
    }

    suspend fun findByIIN(iin: String): Result<EmployeeResult>{
        SharedPrefs.clearUrl(App.appContext)
        val response = RetrofitClient.getApiService().getEmployeeByIIN(iin)
        if(response.isSuccess()){
            val baseUrl = response.asSuccess().headers!![Config.BASE_URL_KEY]!!
            SharedPrefs.saveUrl(baseUrl, App.appContext)
        }
        return  response
    }

}