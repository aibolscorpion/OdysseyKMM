package kz.divtech.odyssey.rotation.ui.login.send_sms

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.login.login.*
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse
import kz.divtech.odyssey.rotation.data.repository.ProfileRepository
import kz.divtech.odyssey.rotation.data.repository.LoginRepository
import kz.divtech.odyssey.rotation.common.utils.Event
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.LoginResponse
import kz.divtech.odyssey.rotation.data.local.SharedPrefsManager.saveAuthToken
import kz.divtech.odyssey.rotation.data.local.SharedPrefsManager.saveOrganizationName
import javax.inject.Inject

@HiltViewModel
class SendSmsViewModel @Inject constructor(private val profileRepository: ProfileRepository,
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
                saveAuthToken(loginResponse.token)
                saveOrganizationName(loginResponse.organization)
                profileRepository.insertProfile(loginResponse.employee)
            }
            _loginResult.value = Event(response)
            pBarVisibility.set(View.GONE)
        }
    }


}