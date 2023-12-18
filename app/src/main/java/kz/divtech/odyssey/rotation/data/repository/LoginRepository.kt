package kz.divtech.odyssey.rotation.data.repository

import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService
import kz.divtech.odyssey.rotation.domain.model.login.login.AuthRequest
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.LoginResponse
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeRequest
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse

class LoginRepository(private val apiService: ApiService) {

    suspend fun requestSmsCode(phoneNumber: String): Result<CodeResponse> {
        val codeRequest = CodeRequest(phoneNumber, Config.IS_TEST)
        return apiService.sendSms(codeRequest)
    }

    suspend fun login(authRequest: AuthRequest): Result<LoginResponse>{
        return apiService.login(authRequest)
    }

    suspend fun logoutFromServer(){
        apiService.logout()
    }
}