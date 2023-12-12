package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.data.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.common.utils.Event
import okhttp3.ResponseBody
import kz.divtech.odyssey.rotation.data.remote.result.*
import javax.inject.Inject

@HiltViewModel
class PersonalDataViewModel @Inject constructor(val employeeRepository: EmployeeRepository): ViewModel() {
    val employee = employeeRepository.employee
    val pBarVisibility = ObservableInt(View.GONE)

    private var _personalDataUpdated = MutableLiveData<Event<Boolean>>()
    val personalDataUpdated: LiveData<Event<Boolean>> = _personalDataUpdated

    private val _updatePersonalResult = MutableLiveData<Result<ResponseBody>>()
    val updatePersonalResult: LiveData<Result<ResponseBody>> = _updatePersonalResult


    fun updatePersonalData(employee: Employee, citizenshipChanged: Boolean){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val response = employeeRepository.updateEmployee(employee)
            if(response.isSuccess()){
                employeeRepository.getAndInstertEmployee()
                _personalDataUpdated.value = Event(citizenshipChanged)
            }
            _updatePersonalResult.value = response
            pBarVisibility.set(View.GONE)
        }
    }
}