package kz.divtech.odyssey.rotation.ui.login.update_phone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.update_phone.UpdatePhoneRequest

class UpdatePhoneViewModel(app: Application) : AndroidViewModel(app) {
    private val _isApplicationSent = MutableLiveData<Boolean>()
    val isApplicationSent = _isApplicationSent

    private val _message = MutableLiveData<String>()
    val message = _message

    fun updatePhoneNumber(request: UpdatePhoneRequest) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.getApiService().updatePhoneNumber(request)
                when (response.code()) {
                    Constants.SUCCESS_CODE -> _isApplicationSent.postValue(true)
                    Constants.UNPROCESSABLE_ENTITY_CODE -> {
                        val message =
                            getApplication<Application>().getString(R.string.invalid_format_phone_number)
                        _message.postValue(message)
                    }
                }
            } catch (e: Exception) {
                _isApplicationSent.postValue(false)
            }
        }
    }
}