package kz.divtech.odyssey.rotation.ui.login.update_phone

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.login.update_phone.UpdatePhoneRequest
import kz.divtech.odyssey.rotation.data.repository.EmployeeRepository
import okhttp3.ResponseBody
import kz.divtech.odyssey.rotation.data.remote.result.*
import javax.inject.Inject

@HiltViewModel
class UpdatePhoneViewModel @Inject constructor(val employeeRepository: EmployeeRepository) : ViewModel() {
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

}