package kz.divtech.odyssey.rotation.domain.repository

import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Login
import kz.divtech.odyssey.rotation.domain.model.login.login.LoginResponse
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse

class LoginRepository {

    suspend fun requestSmsCode(phoneNumber: String): Result<CodeResponse> {
        val phoneHashMap = mutableMapOf(Constants.PHONE to phoneNumber,
            Config.REQUEST_TYPE to Constants.TEST)
        return RetrofitClient.getApiService().sendSms(phoneHashMap)
    }

    suspend fun login(login: Login): Result<LoginResponse>{
        return RetrofitClient.getApiService().login(login)
    }
}