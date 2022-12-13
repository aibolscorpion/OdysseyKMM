package kz.divtech.odyssey.rotation.ui.login.auth

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.*
import kz.divtech.odyssey.rotation.domain.model.login.search_by_iin.EmployeeData
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse
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

    val pBarVisibility = ObservableInt(View.GONE)

    private val _codeResponse = MutableLiveData<Event<BadRequest>>()
    val codeResponse: LiveData<Event<BadRequest>> = _codeResponse

    private val phoneHashMap = HashMap<String, String>()

    fun sendSmsToPhone(phoneNumber: String?){
            requestSmsCode(phoneNumber!!)
            this.phoneNumber = phoneNumber
    }

    fun sendSmsToPhone(){
        if(this.phoneNumber != null) requestSmsCode(this.phoneNumber!!)
    }

    private fun requestSmsCode(phoneNumber: String){
        pBarVisibility.set(View.VISIBLE)
        phoneHashMap[Constants.PHONE] = phoneNumber
        RetrofitClient.getApiService().sendSms(phoneHashMap).enqueue(object : Callback<CodeResponse>{
            override fun onResponse(call: Call<CodeResponse>, response: Response<CodeResponse>) {
                pBarVisibility.set(View.GONE)
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
            }

            override fun onFailure(call: Call<CodeResponse>, t: Throwable) {
                _isErrorHappened.postValue(Event(true))
                pBarVisibility.set(View.GONE)
            }

        })
    }

    fun login(code: String){
        pBarVisibility.set(View.VISIBLE)
        val login = Login(phoneNumber, code, authLogId)
        RetrofitClient.getApiService().login(login).enqueue(object: Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                pBarVisibility.set(View.GONE)
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
                pBarVisibility.set(View.GONE)
            }

        })
    }

    fun getEmployeeInfoByPhoneNumber(phoneNumber: String){
        pBarVisibility.set(View.VISIBLE)
        RetrofitClient.getApiService().getEmployeeByPhone(phoneNumber).enqueue(object: Callback<EmployeeData>{
            override fun onResponse(call: Call<EmployeeData>, response: Response<EmployeeData>) {
                pBarVisibility.set(View.GONE)
                if(response.code() == 200)
                    _employeeInfo.postValue(Event(response.body()?.data?.employee!!))
                else if(response.code() == 400)
                    _isEmployeeNotFounded.postValue(Event(true))
            }

            override fun onFailure(call: Call<EmployeeData>, t: Throwable) {
                _isErrorHappened.postValue(Event(true))
                pBarVisibility.set(View.GONE)
            }

        })
    }

}