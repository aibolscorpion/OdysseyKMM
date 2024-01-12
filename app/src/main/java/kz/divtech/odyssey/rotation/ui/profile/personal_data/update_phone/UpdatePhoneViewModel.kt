package kz.divtech.odyssey.rotation.ui.profile.personal_data.update_phone

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.utils.Event
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.auth.login.AuthRequest
import kz.divtech.odyssey.shared.domain.model.auth.sendsms.CodeResponse
import kz.divtech.odyssey.shared.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class UpdatePhoneViewModel @Inject constructor(val profileRepository: ProfileRepository): ViewModel() {
    private var authLogId: Int = 0

    private val _smsCodeResult = MutableLiveData<Event<Resource<CodeResponse>>>()
    val smsCodeResult: LiveData<Event<Resource<CodeResponse>>> = _smsCodeResult

    private val _updatedResult = MutableLiveData<Event<Resource<HttpResponse>>>()
    val updatedResult: LiveData<Event<Resource<HttpResponse>>> = _updatedResult

    val pBarVisibility = ObservableInt(View.GONE)

    fun setAuthLogId(authLogId: Int){
        this.authLogId = authLogId
    }

    fun requestSmsCode(phoneNumber: String){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val response = profileRepository.updatePhoneNumberWithAuth(phoneNumber)
            _smsCodeResult.value = Event(response)
            pBarVisibility.set(View.GONE)
        }
    }

    fun confirmUpdate(code: String){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val authRequest = AuthRequest("", code, authLogId)
            val response = profileRepository.updatePhoneConfirm(authRequest)
            _updatedResult.value = Event(response)
            pBarVisibility.set(View.GONE)
        }
    }


}