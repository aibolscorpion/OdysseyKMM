package kz.divtech.odyssey.rotation.ui.profile.personal_data.update_phone

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Config
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.result.asFailure
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isHttpException
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.AuthRequest
import kz.divtech.odyssey.rotation.domain.model.login.sendsms.CodeRequest
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.utils.Event

class UpdatePhoneViewModel(val employeeRepository: EmployeeRepository): ViewModel() {
    private var authLogId: Int = 0

    private val _smsCodeSent = MutableLiveData<Event<Boolean>>()
    val smsCodeSent: LiveData<Event<Boolean>> = _smsCodeSent

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>> = _errorMessage

    private val _successfullyUpdated = MutableLiveData<Event<Boolean>>()
    val successfullyUpdated : LiveData<Event<Boolean>> = _successfullyUpdated

    private val _secondsMutableLiveData = MutableLiveData<Event<Int>>()
    val secondsLiveData: LiveData<Event<Int>> = _secondsMutableLiveData

    val pBarVisibility = ObservableInt(View.GONE)

    fun requestSmsCode(phoneNumber: String){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val codeRequest = CodeRequest(phoneNumber, Config.IS_TEST)
            val response = RetrofitClient.getApiService().updatePhoneNumberWithAuth(codeRequest)
            if(response.isSuccess()) {
                authLogId = response.asSuccess().value.auth_log_id
                _smsCodeSent.postValue(Event(true))
            }else if(response.isHttpException()){
                if(response.statusCode == Constants.TOO_MANY_REQUEST_CODE){
                    val seconds = Integer.valueOf(response.headers?.get(Constants.RETRY_AFTER)!!)
                    _secondsMutableLiveData.postValue(Event(seconds))
                }
            }else{
                _errorMessage.postValue(Event((response.asFailure().error?.message!!)))
            }
            pBarVisibility.set(View.GONE)
        }
    }

    fun confirmUpdate(code: String){
        pBarVisibility.set(View.VISIBLE)
        val authRequest = AuthRequest("", code, authLogId)

        viewModelScope.launch {
            val response = RetrofitClient.getApiService().updatePhoneConfirm(authRequest)
            if(response.isSuccess()) {
                employeeRepository.getEmployeeFromServer()
                _successfullyUpdated.postValue(Event(true))
            }else if(response.isHttpException()) {
                if (response.statusCode == Constants.BAD_REQUEST_CODE) {
                    _errorMessage.postValue(
                        Event(App.appContext.getString(R.string.filled_incorrect_code))
                    )
                }else if(response.statusCode == Constants.TOO_MANY_REQUEST_CODE) {
                    val seconds = Integer.valueOf(response.headers?.get(Constants.RETRY_AFTER)!!)
                    _errorMessage.postValue(
                        Event(App.appContext.getString(R.string.too_many_request_message, seconds))
                    )
                }
            }else{
                _errorMessage.postValue(Event(response.asFailure().error?.message!!))
            }

            pBarVisibility.set(View.GONE)
        }
    }

    class UpdatePhoneViewModelFactory(private val repository: EmployeeRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(UpdatePhoneViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return UpdatePhoneViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}