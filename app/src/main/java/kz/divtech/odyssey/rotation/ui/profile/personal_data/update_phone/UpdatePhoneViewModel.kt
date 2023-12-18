package kz.divtech.odyssey.rotation.ui.profile.personal_data.update_phone

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.Config
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.login.login.AuthRequest
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeRequest
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeResponse
import kz.divtech.odyssey.rotation.data.repository.ProfileRepository
import kz.divtech.odyssey.rotation.common.utils.Event
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class UpdatePhoneViewModel @Inject constructor(val profileRepository: ProfileRepository,
                                               private val apiService: ApiService): ViewModel() {
    private var authLogId: Int = 0

    private val _smsCodeResult = MutableLiveData<Event<Result<CodeResponse>>>()
    val smsCodeResult: LiveData<Event<Result<CodeResponse>>> = _smsCodeResult

    private val _updatedResult = MutableLiveData<Event<Result<ResponseBody>>>()
    val updatedResult: LiveData<Event<Result<ResponseBody>>> = _updatedResult

    val pBarVisibility = ObservableInt(View.GONE)

    fun setAuthLogId(authLogId: Int){
        this.authLogId = authLogId
    }

    fun requestSmsCode(phoneNumber: String){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val codeRequest = CodeRequest(phoneNumber, Config.IS_TEST)
            val response = apiService.updatePhoneNumberWithAuth(codeRequest)
            _smsCodeResult.value = Event(response)
            pBarVisibility.set(View.GONE)
        }
    }

    fun confirmUpdate(code: String){
        pBarVisibility.set(View.VISIBLE)
        val authRequest = AuthRequest("", code, authLogId)

        viewModelScope.launch {
            val response = apiService.updatePhoneConfirm(authRequest)
            if(response.isSuccess()) {
                profileRepository.getAndInsertProfile()
            }
            _updatedResult.value = Event(response)
            pBarVisibility.set(View.GONE)
        }
    }


}