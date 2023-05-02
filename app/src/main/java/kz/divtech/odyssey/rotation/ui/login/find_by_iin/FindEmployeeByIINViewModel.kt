package kz.divtech.odyssey.rotation.ui.login.find_by_iin

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeShort
import kz.divtech.odyssey.rotation.domain.repository.FindEmployeeRepository
import kz.divtech.odyssey.rotation.utils.Event

class FindEmployeeByIINViewModel(private val findEmployeeRepository: FindEmployeeRepository) : ViewModel() {
    private val _employeeData = MutableLiveData<Event<EmployeeShort>>()
    val employeeData: LiveData<Event<EmployeeShort>> = _employeeData

    private val _isEmployeeNotFounded = MutableLiveData<Event<Boolean>>()
    val isEmployeeNotFounded: LiveData<Event<Boolean>> = _isEmployeeNotFounded

    val pBarVisibility = ObservableInt(View.GONE)

    fun findByIIN(iin: String){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val response = findEmployeeRepository.findByIIN(iin)
            if(response.isSuccess()){
                val employeeResult = response.asSuccess().value
                if(employeeResult.exists){
                    _employeeData.value = Event(employeeResult.employee)
                }else{
                    _isEmployeeNotFounded.value = Event(true)
                }
            }
            pBarVisibility.set(View.GONE)
        }
    }


    class FindEmployeeByIINViewModelFactory(private val findEmployeeRepository: FindEmployeeRepository): ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(FindEmployeeByIINViewModel::class.java)){
                return FindEmployeeByIINViewModel(findEmployeeRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}