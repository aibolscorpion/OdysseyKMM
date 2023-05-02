package kz.divtech.odyssey.rotation.ui.login.find_by_phone_number

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.rotation.domain.model.login.search_employee.EmployeeShort
import kz.divtech.odyssey.rotation.domain.repository.FindEmployeeRepository
import kz.divtech.odyssey.rotation.domain.repository.OrgInfoRepository
import kz.divtech.odyssey.rotation.utils.Event

class FindEmployeeViewModel(private val findEmployeeRepository: FindEmployeeRepository,
                        private val orgInfoRepository: OrgInfoRepository) : ViewModel() {
    private val _isEmployeeNotFounded = MutableLiveData<Event<Boolean>>()
    val isEmployeeNotFounded : LiveData<Event<Boolean>> = _isEmployeeNotFounded

    private val _employeeInfo = MutableLiveData<Event<EmployeeShort>>()
    val employeeInfo : LiveData<Event<EmployeeShort>> = _employeeInfo

    private val _isErrorHappened = MutableLiveData<Event<Boolean>>()
    val isErrorHappened: LiveData<Event<Boolean>> = _isErrorHappened

    val pBarVisibility = ObservableInt(View.GONE)

    fun getEmployeeInfoByPhoneNumber(phoneNumber: String){
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            val result = findEmployeeRepository.findByPhoneNumber(phoneNumber)
            if(result.isSuccess()){
                val employeeResult = result.asSuccess().value
                if(employeeResult.exists){
                    _employeeInfo.postValue(Event(employeeResult.employee))
                }else{
                    _isEmployeeNotFounded.postValue(Event(true))
                }
            }else {
                _isErrorHappened.postValue(Event(true))
            }
            pBarVisibility.set(View.GONE)
        }
    }

    fun getOrgInfoFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            orgInfoRepository.getOrgInfoFromServer()
            pBarVisibility.set(View.GONE)
        }


    class FindEmployeeViewModelFactory(private val findEmployeeRepository: FindEmployeeRepository,
        private val orgInfoRepository: OrgInfoRepository): ViewModelProvider.Factory{
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(FindEmployeeViewModel::class.java)){
                return FindEmployeeViewModel(findEmployeeRepository, orgInfoRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }



}