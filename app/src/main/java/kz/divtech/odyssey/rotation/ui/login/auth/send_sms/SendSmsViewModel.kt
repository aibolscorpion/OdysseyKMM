package kz.divtech.odyssey.rotation.ui.login.auth.send_sms

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.*
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse
import kz.divtech.odyssey.rotation.domain.repository.ApplicationsRepository
import kz.divtech.odyssey.rotation.utils.SharedPrefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendSmsViewModel(val repository: ApplicationsRepository) : ViewModel() {
    var authLogId: String? = null

    private val _smsCodeSent = MutableLiveData<Boolean>()
    val smsCodeSent: LiveData<Boolean> = _smsCodeSent

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn : LiveData<Boolean> = _loggedIn

    private val _secondsMutableLiveData = MutableLiveData<Int>()
    val secondsLiveData: LiveData<Int> = _secondsMutableLiveData

    val pBarVisibility = ObservableInt(View.GONE)

    fun requestSmsCode(phoneNumber: String){
        pBarVisibility.set(View.VISIBLE)

        val phoneHashMap = HashMap<String, String>()
        phoneHashMap[Constants.PHONE] = phoneNumber
        phoneHashMap[Config.REQUEST_TYPE] = Constants.TEST

        RetrofitClient.getApiService().sendSms(phoneHashMap).enqueue(object :
            Callback<CodeResponse> {
            override fun onResponse(call: Call<CodeResponse>, response: Response<CodeResponse>) {
                pBarVisibility.set(View.GONE)
                when(response.code()){
                    Constants.SUCCESS_CODE -> {
                        authLogId = response.body()?.data?.auth_log_id
                        _smsCodeSent.postValue(true)
                    }
                    Constants.TOO_MANY_REQUEST_CODE -> {
                        val seconds = Integer.valueOf(response.headers()[Constants.RETRY_AFTER]!!)
                        _secondsMutableLiveData.postValue(seconds)
                    }
                }
            }

            override fun onFailure(call: Call<CodeResponse>, t: Throwable) {
                _errorMessage.postValue(t.message!!)
                pBarVisibility.set(View.GONE)
            }

        })
    }

    fun login(phoneNumber: String, code: String){
        pBarVisibility.set(View.VISIBLE)
        val login = Login(phoneNumber, code, authLogId)
        RetrofitClient.getApiService().login(login).enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                pBarVisibility.set(View.GONE)
                when (response.code()) {
                    Constants.SUCCESS_CODE -> {
                        val loginResponse = response.body()
                        _loggedIn.postValue(true)
                        SharedPrefs().saveAuthToken(loginResponse?.data?.token!!)
                        loginResponse.data.employee.let { employee -> insertEmployeeToDB(employee) }
                    }
                    Constants.BAD_REQUEST_CODE -> {
                        _errorMessage.postValue(
                            App.appContext.getString(R.string.filled_incorrect_code)
                        )
                    }
                    Constants.TOO_MANY_REQUEST_CODE -> {
                        val seconds = Integer.valueOf(response.headers()[Constants.RETRY_AFTER]!!)
                        _errorMessage.postValue(
                            App.appContext.getString(R.string.too_many_request_message, seconds)
                        )
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _errorMessage.postValue(t.message!!)
                pBarVisibility.set(View.GONE)
            }

        })
    }

    class FillCodeViewModelFactory(private val repository: ApplicationsRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(SendSmsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return SendSmsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    fun insertEmployeeToDB(employee: Employee) = viewModelScope.launch {
        repository.insertEmployee(employee)
    }
}