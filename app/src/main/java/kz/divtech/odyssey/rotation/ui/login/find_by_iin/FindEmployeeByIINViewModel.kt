package kz.divtech.odyssey.rotation.ui.login.find_by_iin

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.result.asSuccess
import kz.divtech.odyssey.rotation.data.remote.result.isHttpException
import kz.divtech.odyssey.rotation.data.remote.result.isSuccess
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.repository.FindEmployeeRepository
import kz.divtech.odyssey.rotation.utils.Event

class FindEmployeeByIINViewModel(private val findEmployeeRepository: FindEmployeeRepository) : ViewModel() {
    private val _employeeData = MutableLiveData<Event<Employee>>()
    val employeeData: LiveData<Event<Employee>> = _employeeData

    private val _isEmployeeNotFounded = MutableLiveData<Event<Boolean>>()
    val isEmployeeNotFounded = _isEmployeeNotFounded

    val pBarVisibility = ObservableInt(View.GONE)

    fun findByIIN(iin: String){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val response = findEmployeeRepository.findByIIN(iin)
            if(response.isSuccess()){
                _employeeData.postValue(Event(response.asSuccess().value.data.employee!!))
            }else if(response.isHttpException()){
                if(response.statusCode == Constants.BAD_REQUEST_CODE){
                    isEmployeeNotFounded.postValue(Event(true))
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