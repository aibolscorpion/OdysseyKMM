package kz.divtech.odyssey.shared.data.repository

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import kz.divtech.odyssey.shared.data.remote.HttpRoutes
import kz.divtech.odyssey.shared.data.remote.MainApi
import kz.divtech.odyssey.shared.domain.EmployeeResult

class FindEmployeeRepository() {
    private val httpClient = MainApi.httpClient
    suspend fun findByPhoneNumber(phoneNumber: String) : EmployeeResult{
        return httpClient.get{
            url(HttpRoutes.GET_EMPLOYEE_BY_PHONE)
            parameter("phone", phoneNumber)
        }.body()
    }



}