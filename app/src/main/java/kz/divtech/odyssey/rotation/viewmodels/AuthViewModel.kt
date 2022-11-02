package kz.divtech.odyssey.rotation.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.Config
import kz.divtech.odyssey.rotation.api.RetrofitClient
import kz.divtech.odyssey.rotation.models.authentication.sendsms.CodeResponse
import kz.divtech.odyssey.rotation.models.authentication.login.Login
import kz.divtech.odyssey.rotation.models.authentication.login.LoginResponse
import kz.divtech.odyssey.rotation.models.authentication.sendsms.PhoneNumber
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

open class AuthViewModel : ViewModel() {
    var authLogId: String? = null
    var phoneNumber: String? = null
    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    fun sendSmsToPhone(phoneNumber: String?){
        if(phoneNumber == null && this.phoneNumber != null)
            requestSmsCode(this.phoneNumber!!)
        else{
            requestSmsCode(phoneNumber!!)
            this.phoneNumber = phoneNumber
        }
    }

    private fun requestSmsCode(phoneNumber: String){
        RetrofitClient.getApiService().sendSms(Config.DEVICE_ID, PhoneNumber(phoneNumber)).enqueue(object : Callback<CodeResponse>{
            override fun onResponse(call: Call<CodeResponse>, response: Response<CodeResponse>) {
                if(response.isSuccessful){
                    val codeResponse  = response.body()
                    authLogId = codeResponse?.data?.auth_log_id
                    _message.postValue(codeResponse?.message!!)
                }
            }

            override fun onFailure(call: Call<CodeResponse>, t: Throwable) {
            }

        })
    }

    fun login(code: String){
        val login = Login(phoneNumber, code, authLogId)
        RetrofitClient.getApiService().login(Config.DEVICE_ID, login).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val loginResponse = response.body()
                    _message.postValue(loginResponse?.message!!)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {

            }

        })
    }

}