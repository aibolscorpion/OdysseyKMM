package kz.divtech.odyssey.rotation.ui.login.find_by_phone_number

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.utils.Event
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.auth.search_employee.EmployeeResult
import kz.divtech.odyssey.shared.domain.repository.FindEmployeeRepository
import kz.divtech.odyssey.shared.domain.repository.OrgInfoRepository
import javax.inject.Inject

@HiltViewModel
class FindEmployeeViewModel @Inject constructor(private val findEmployeeRepository: FindEmployeeRepository,
                                                private val orgInfoRepository: OrgInfoRepository
) : ViewModel() {
    private val _employeeResult = MutableLiveData<Event<Resource<EmployeeResult>>>()
    val employeeResult : LiveData<Event<Resource<EmployeeResult>>> = _employeeResult

    val pBarVisibility = ObservableInt(View.GONE)

    fun getEmployeeInfoByPhoneNumber(phoneNumber: String){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val result = findEmployeeRepository.findByPhoneNumber(phoneNumber)
            _employeeResult.value = Event(result)
            pBarVisibility.set(View.GONE)
        }
    }

    fun getOrgInfoFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            orgInfoRepository.getOrgInfo()
            pBarVisibility.set(View.GONE)
        }

}