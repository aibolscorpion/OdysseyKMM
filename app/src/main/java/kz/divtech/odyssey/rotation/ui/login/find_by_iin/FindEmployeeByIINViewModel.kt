package kz.divtech.odyssey.rotation.ui.login.find_by_iin

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.utils.Event
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.auth.search_employee.EmployeeResult
import kz.divtech.odyssey.shared.domain.repository.FindEmployeeRepository
import javax.inject.Inject

@HiltViewModel
class FindEmployeeByIINViewModel @Inject constructor(private val findEmployeeRepository: FindEmployeeRepository) : ViewModel() {
    private val _employeeResult = MutableLiveData<Event<Resource<EmployeeResult>>>()
    val employeeResult: LiveData<Event<Resource<EmployeeResult>>> = _employeeResult

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