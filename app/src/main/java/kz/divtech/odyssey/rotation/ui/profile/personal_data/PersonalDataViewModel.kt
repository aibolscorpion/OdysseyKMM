package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import com.google.gson.Gson
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.Constants.UNPROCESSABLE_ENTITY_CODE
import kz.divtech.odyssey.rotation.data.remote.result.isHttpException
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.fdf.ValidationErrorResponse
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.utils.Event

class PersonalDataViewModel(val employeeRepository: EmployeeRepository): ViewModel() {
    val employee = employeeRepository.employee.asLiveData()
    val pBarVisibility = ObservableInt(View.GONE)

    private var _personalDataUpdated = MutableLiveData<Event<Boolean>>()
    val personalDataUpdated: LiveData<Event<Boolean>> = _personalDataUpdated

    private val _validationErrors: MutableLiveData<ValidationErrorResponse> = MutableLiveData()
    val validationErrors: LiveData<ValidationErrorResponse> = _validationErrors


    fun updatePersonalData(employee: Employee, citizenshipChanged: Boolean){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            val response = employeeRepository.updateEmployee(employee)
            if(response.isSuccess()){
                employeeRepository.getEmployeeFromServer()
                _personalDataUpdated.value = Event(citizenshipChanged)
            }else if(response.isHttpException()) {
                if (response.statusCode == UNPROCESSABLE_ENTITY_CODE) {
                    val errorResponse =
                        Gson().fromJson(response.error.errorBody?.string(), ValidationErrorResponse::class.java)
                        _validationErrors.value = errorResponse
                }
            }
            pBarVisibility.set(View.GONE)
        }
    }

    class PersonalDataViewModelFactory(private val repository: EmployeeRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(PersonalDataViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return PersonalDataViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}