package kz.divtech.odyssey.rotation.ui.login.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse
import kz.divtech.odyssey.rotation.domain.model.login.login.Login
import kz.divtech.odyssey.rotation.domain.model.login.login.LoginResponse
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.PhoneNumber
import kz.divtech.odyssey.rotation.utils.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

open class AuthViewModel : ViewModel() {
    var authLogId: String? = null
    var phoneNumber: String? = null

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String> = _message

    private val _isSuccessfullyLoggedIn = MutableLiveData<Boolean>()
    val isSuccessfullyLoggedIn : LiveData<Boolean> = _isSuccessfullyLoggedIn

    fun sendSmsToPhone(phoneNumber: String?){
        if(phoneNumber == null && this.phoneNumber != null)
            requestSmsCode(this.phoneNumber!!)
        else{
            requestSmsCode(phoneNumber!!)
            this.phoneNumber = phoneNumber
        }
    }

    private fun requestSmsCode(phoneNumber: String){
        RetrofitClient.getApiService().sendSms(PhoneNumber(phoneNumber)).enqueue(object : Callback<CodeResponse>{
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
        RetrofitClient.getApiService().login(login).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    val loginResponse = response.body()
                    Timber.d(loginResponse.toString())
                    SessionManager().saveAuthToken(loginResponse?.data?.token!!)
                    _message.postValue(loginResponse.message!!)
                    _isSuccessfullyLoggedIn.postValue(true)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Timber.d(call.toString())

            }

        })
    }

}