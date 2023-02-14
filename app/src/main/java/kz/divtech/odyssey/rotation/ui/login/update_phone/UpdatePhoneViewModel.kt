package kz.divtech.odyssey.rotation.ui.login.update_phone

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.result.isHttpException
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.login.update_phone.UpdatePhoneRequest
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository

class UpdatePhoneViewModel(val employeeRepository: EmployeeRepository) : ViewModel() {
    private val _isApplicationSent = MutableLiveData<Boolean>()
    val isApplicationSent = _isApplicationSent

    private val _message = MutableLiveData<String>()
    val message = _message

    val pBarVisibility = ObservableInt(View.GONE)

    fun updatePhoneNumber(request: UpdatePhoneRequest) {
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
                val response = employeeRepository.updatePhoneNumber(request)
                if(response.isSuccess()) {
                    _isApplicationSent.postValue(true)
                }else if(response.isHttpException()){
                    if(response.statusCode == Constants.UNPROCESSABLE_ENTITY_CODE){
                        val message =
                            App.appContext.getString(R.string.invalid_format_phone_number)
                        _message.postValue(message)
                    }
                }else{
                    _isApplicationSent.postValue(false)
                }
            pBarVisibility.set(View.GONE)
        }
    }

    class UpdatePhoneViewModelFactory(private val employeeRepository: EmployeeRepository)
        : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(UpdatePhoneViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return UpdatePhoneViewModel(employeeRepository) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }
}