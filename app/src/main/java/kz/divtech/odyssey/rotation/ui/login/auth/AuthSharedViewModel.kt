package kz.divtech.odyssey.rotation.ui.login.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.*
import kz.divtech.odyssey.rotation.domain.model.login.search_by_iin.EmployeeData
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.PhoneNumber
import kz.divtech.odyssey.rotation.utils.Event
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class AuthSharedViewModel : ViewModel() {
    var authLogId: String? = null
    var phoneNumber: String? = null

    private val _message = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>> = _message

    private val _smsCodeSent = MutableLiveData<Event<Boolean>>()
    val smsCodeSent: LiveData<Event<Boolean>> = _smsCodeSent

    private val _isEmployeeNotFounded = MutableLiveData<Event<Boolean>>()
    val isEmployeeNotFounded : LiveData<Event<Boolean>> = _isEmployeeNotFounded

    private val _employeeInfo = MutableLiveData<Event<Employee>>()
    val employeeInfo : LiveData<Event<Employee>> = _employeeInfo

    private val _isSuccessfullyLoggedIn = MutableLiveData<Boolean>()
    val isSuccessfullyLoggedIn : LiveData<Boolean> = _isSuccessfullyLoggedIn

    private val _isErrorHappened = MutableLiveData<Event<Boolean>>()
    val isErrorHappened: LiveData<Event<Boolean>> = _isErrorHappened

    private val _showProgressBar = MutableLiveData<Boolean>()
    val showProgressBar: LiveData<Boolean> = _showProgressBar

    private val _codeResponse = MutableLiveData<Event<BadRequest>>()
    val codeResponse: LiveData<Event<BadRequest>> = _codeResponse

    fun sendSmsToPhone(phoneNumber: String?){
            requestSmsCode(phoneNumber!!)
            this.phoneNumber = phoneNumber
    }

    fun sendSmsToPhone(){
        if(this.phoneNumber != null) requestSmsCode(this.phoneNumber!!)
    }

    private fun requestSmsCode(phoneNumber: String){
        _showProgressBar.postValue(true)
        RetrofitClient.getApiService().sendSms(PhoneNumber(phoneNumber)).enqueue(object : Callback<CodeResponse>{
            override fun onResponse(call: Call<CodeResponse>, response: Response<CodeResponse>) {
                when(response.code()){
                    200 -> {
                        val codeResponse  = response.body()
                        authLogId = codeResponse?.data?.auth_log_id
                        _smsCodeSent.postValue(Event(true))
                    }
                    400 -> {
                        lateinit var mError : BadRequest
                        try {
                            mError = GsonBuilder().create().fromJson(response.errorBody()!!.string(),BadRequest::class.java)
                        } catch (_: IOException) {}
                        when(mError.slug){
                            Constants.EMPLOYEE_NOT_FOUND -> _smsCodeSent.postValue(Event(false))
                            Constants.INVALID_PHONE_NUMBER -> _message.postValue(Event(mError.message!!))
                        }
                    }
                    429 -> {
                        lateinit var mError : TooManyRequest
                        try {
                            mError = GsonBuilder().create().fromJson(response.errorBody()!!.string(),TooManyRequest::class.java)
                        } catch (_: IOException) {}
                        _message.postValue(Event(mError.message!!))
                    }

                }
                _showProgressBar.postValue(false)
            }

            override fun onFailure(call: Call<CodeResponse>, t: Throwable) {
                _isErrorHappened.postValue(Event(true))
                _showProgressBar.postValue(false)
            }

        })
    }

    fun login(code: String){
        _showProgressBar.value = true
        val login = Login(phoneNumber, code, authLogId)
        RetrofitClient.getApiService().login(login).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                _showProgressBar.postValue(false)
                if(response.isSuccessful){
                        val loginResponse = response.body()
                        SharedPrefs().saveAuthToken(loginResponse?.data?.token!!)
                        _isSuccessfullyLoggedIn.postValue(true)
                }else{
                    lateinit var mError : BadRequest
                    try {
                        mError = GsonBuilder().create().fromJson(response.errorBody()!!.string(), BadRequest::class.java)
                    } catch (_: IOException) {}
                    when(response.code()){
                        400 -> {
                            when(mError.slug){
                                Constants.INCORRECT_DATA -> _codeResponse.postValue(Event(mError))
                            }
                        }
                        429 -> {
                            _codeResponse.postValue(Event(mError))
                        }
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _showProgressBar.postValue(false)
            }

        })
    }

    fun getEmployeeInfoByPhoneNumber(phoneNumber: String){
        _showProgressBar.postValue(true)
        RetrofitClient.getApiService().getEmployeeByPhone(phoneNumber).enqueue(object: Callback<EmployeeData>{
            override fun onResponse(call: Call<EmployeeData>, response: Response<EmployeeData>) {
                _showProgressBar.postValue(false)
                if(response.code() == 200)
                    _employeeInfo.postValue(Event(response.body()?.data?.employee!!))
                else if(response.code() == 400)
                    _isEmployeeNotFounded.postValue(Event(true))
            }

            override fun onFailure(call: Call<EmployeeData>, t: Throwable) {
                _isErrorHappened.postValue(Event(true))
                _showProgressBar.postValue(false)
            }

        })
    }

}