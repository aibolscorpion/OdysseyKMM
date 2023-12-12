package kz.divtech.odyssey.rotation.ui.login.find_by_iin

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeResult
import kz.divtech.odyssey.rotation.data.repository.FindEmployeeRepository
import kz.divtech.odyssey.rotation.common.utils.Event
import javax.inject.Inject

@HiltViewModel
class FindEmployeeByIINViewModel @Inject constructor(private val findEmployeeRepository: FindEmployeeRepository) : ViewModel() {
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

}