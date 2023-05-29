package kz.divtech.odyssey.rotation.ui.login.find_by_iin

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeResult
import kz.divtech.odyssey.rotation.domain.repository.FindEmployeeRepository
import kz.divtech.odyssey.rotation.utils.Event

class FindEmployeeByIINViewModel(private val findEmployeeRepository: FindEmployeeRepository) : ViewModel() {
    private val _employeeResult = MutableLiveData<Event<Result<EmployeeResult>>>()
    val employeeResult: LiveData<Event<Result<EmployeeResult>>> = _employeeResult

    val pBarVisibility = ObservableInt(View.GONE)

    fun findByIIN(iin: String){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val response = findEmployeeRepository.findByIIN(iin)
            _employeeResult.value = Event(response)
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