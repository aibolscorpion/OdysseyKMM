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
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.utils.Event
import kz.divtech.odyssey.rotation.utils.SharedPrefs

class SendSmsViewModel(private val employeeRepository: EmployeeRepository) : ViewModel() {
    var authLogId: String? = null

    private val _smsCodeSent = MutableLiveData<Event<Boolean>>()
    val smsCodeSent: LiveData<Event<Boolean>> = _smsCodeSent

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    private val _loggedIn = MutableLiveData<Event<Boolean>>()
    val loggedIn : LiveData<Event<Boolean>> = _loggedIn

    private val _secondsMutableLiveData = MutableLiveData<Event<Int>>()
    val secondsLiveData: LiveData<Event<Int>> = _secondsMutableLiveData

    val pBarVisibility = ObservableInt(View.GONE)

    fun requestSmsCode(phoneNumber: String){
        pBarVisibility.set(View.VISIBLE)

        val phoneHashMap = HashMap<String, String>()
        phoneHashMap[Constants.PHONE] = phoneNumber
        phoneHashMap[Config.REQUEST_TYPE] = Constants.TEST

        viewModelScope.launch {
            try {
                val response = RetrofitClient.getApiService().sendSms(phoneHashMap)
                when(response.code()){
                    Constants.SUCCESS_CODE -> {
                        authLogId = response.body()?.data?.auth_log_id
                        _smsCodeSent.postValue(Event(true))
                    }
                    Constants.TOO_MANY_REQUEST_CODE -> {
                        val seconds = Integer.valueOf(response.headers()[Constants.RETRY_AFTER]!!)
                        _secondsMutableLiveData.postValue(Event(seconds))
                    }
                }
            }catch (e: Exception){
                _errorMessage.postValue(Event((e.message.toString())))
            }
            pBarVisibility.set(View.GONE)
        }
    }

    fun login(phoneNumber: String, code: String){
        pBarVisibility.set(View.VISIBLE)
        val login = Login(phoneNumber, code, authLogId)

        viewModelScope.launch {
            try{
                val response = RetrofitClient.getApiService().login(login)
                when (response.code()) {
                    Constants.SUCCESS_CODE -> {
                        val loginResponse = response.body()
                        _loggedIn.postValue(Event(true))
                        SharedPrefs.saveAuthToken(loginResponse?.data?.token!!, App.appContext)
                        loginResponse.data.employee.let { employee -> insertEmployeeToDB(employee) }
                    }
                    Constants.BAD_REQUEST_CODE -> {
                        _errorMessage.postValue(
                            Event(App.appContext.getString(R.string.filled_incorrect_code))
                        )
                    }
                    Constants.TOO_MANY_REQUEST_CODE -> {
                        val seconds = Integer.valueOf(response.headers()[Constants.RETRY_AFTER]!!)
                        _errorMessage.postValue(
                            Event(App.appContext.getString(R.string.too_many_request_message, seconds))
                        )
                    }
                }
            }catch (e: Exception){
                _errorMessage.postValue(Event(e.message!!))
            }
            pBarVisibility.set(View.GONE)
        }
    }

    class FillCodeViewModelFactory(private val employeeRepository: EmployeeRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(SendSmsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return SendSmsViewModel(employeeRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private fun insertEmployeeToDB(employee: Employee) {
        viewModelScope.launch {
            employeeRepository.insertEmployee(employee)
        }
    }
}