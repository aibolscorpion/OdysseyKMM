package kz.divtech.odyssey.rotation.domain.repository

import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.AuthRequest
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.LoginResponse
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeRequest
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse

class LoginRepository {

    suspend fun requestSmsCode(phoneNumber: String): Result<CodeResponse> {
        val codeRequest = CodeRequest(phoneNumber, Config.IS_TEST)
        return RetrofitClient.getApiService().sendSms(codeRequest)
    }

    suspend fun login(authRequest: AuthRequest): Result<LoginResponse>{
        return RetrofitClient.getApiService().login(authRequest)
    }
}