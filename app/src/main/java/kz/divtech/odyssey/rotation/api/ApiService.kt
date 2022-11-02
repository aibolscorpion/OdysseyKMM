package kz.divtech.odyssey.rotation.api

import kz.divtech.odyssey.rotation.models.authentication.sendsms.CodeResponse
import kz.divtech.odyssey.rotation.models.authentication.login.Login
import kz.divtech.odyssey.rotation.models.authentication.login.LoginResponse
import kz.divtech.odyssey.rotation.models.authentication.sendsms.PhoneNumber
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("employees/send-code-login")
    fun sendSms(@Header("deviceid") deviceId: String, @Body phone: PhoneNumber): Call<CodeResponse>

    @POST("employees/login")
    fun login(@Header("deviceid") deviceId: String, @Body login : Login): Call<LoginResponse>

}