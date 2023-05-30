package kz.divtech.odyssey.rotation.ui.login.update_phone

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.login.update_phone.UpdatePhoneRequest
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import okhttp3.ResponseBody
import kz.divtech.odyssey.rotation.data.remote.result.*

class UpdatePhoneViewModel(val employeeRepository: EmployeeRepository) : ViewModel() {
    private val _updatePhoneResult = MutableLiveData<Result<ResponseBody>>()
    val updatePhoneResult: LiveData<Result<ResponseBody>> = _updatePhoneResult

    val pBarVisibility = ObservableInt(View.GONE)

    fun updatePhoneNumber(request: UpdatePhoneRequest) {
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val response = employeeRepository.updatePhoneNumber(request)
            _updatePhoneResult.value = response
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