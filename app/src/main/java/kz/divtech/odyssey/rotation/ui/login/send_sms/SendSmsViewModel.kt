package kz.divtech.odyssey.rotation.ui.login.send_sms

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.utils.Event
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.auth.login.AuthRequest
import kz.divtech.odyssey.shared.domain.model.auth.login.employee_response.LoginResponse
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeResponse
import kz.divtech.odyssey.shared.domain.repository.LoginRepository
import javax.inject.Inject

@HiltViewModel
class SendSmsViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

    private var authLogId: Int = 0

    private val _smsCodeResult = MutableLiveData<Event<Resource<CodeResponse>>>()
    val smsCodeResult: LiveData<Event<Resource<CodeResponse>>> = _smsCodeResult

    private val _loginResult = MutableLiveData<Event<Resource<LoginResponse>>>()
    val loginResult: LiveData<Event<Resource<LoginResponse>>> = _loginResult

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
            _loginResult.value = Event(response)
            pBarVisibility.set(View.GONE)
        }
    }

}