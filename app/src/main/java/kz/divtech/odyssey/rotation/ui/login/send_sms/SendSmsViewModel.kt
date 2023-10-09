package kz.divtech.odyssey.rotation.ui.login.send_sms

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.App
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.login.login.*
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse
import kz.divtech.odyssey.rotation.data.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.data.repository.LoginRepository
import kz.divtech.odyssey.rotation.common.utils.Event
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.LoginResponse
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.saveAuthToken
import kz.divtech.odyssey.rotation.common.utils.SharedPrefs.saveOrganizationName

class SendSmsViewModel(private val employeeRepository: EmployeeRepository,
                       private val loginRepository: LoginRepository
) : ViewModel() {

    private var authLogId: Int = 0

    private val _smsCodeResult = MutableLiveData<Event<Result<CodeResponse>>>()
    val smsCodeResult: LiveData<Event<Result<CodeResponse>>> = _smsCodeResult

    private val _loginResult = MutableLiveData<Event<Result<LoginResponse>>>()
    val loginResult: LiveData<Event<Result<LoginResponse>>> = _loginResult

    val pBarVisibility = ObservableInt(View.GONE)

    fun setAuthLogId(authLogId: Int){
        this.authLogId = authLogId
    }

    fun requestSmsCode(phoneNumber: String){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val response = loginRepository.requestSmsCode(phoneNumber)
            _smsCodeResult.value = Event(response)
            pBarVisibility.set(View.GONE)
        }
    }

    fun login(phoneNumber: String, code: String){
        pBarVisibility.set(View.VISIBLE)
        val authRequest = AuthRequest(phoneNumber, code, authLogId)

        viewModelScope.launch {
            val response = loginRepository.login(authRequest)
            if(response.isSuccess()) {
                val loginResponse = response.asSuccess().value
                App.appContext.saveAuthToken(loginResponse.token)
                App.appContext.saveOrganizationName(loginResponse.organization)
                employeeRepository.insertEmployee(loginResponse.employee)
            }
            _loginResult.value = Event(response)
            pBarVisibility.set(View.GONE)
        }
    }

    class FillCodeViewModelFactory(private val employeeRepository: EmployeeRepository,
                                   private val loginRepository: LoginRepository
    ) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(SendSmsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return SendSmsViewModel(employeeRepository, loginRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}