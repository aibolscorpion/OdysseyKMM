package kz.divtech.odyssey.rotation.ui.login.search_by_iin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.utils.Event

class SearchIINViewModel : ViewModel() {
    private val _employeeData = MutableLiveData<Event<Employee>>()
    val employeeData: LiveData<Event<Employee>> = _employeeData

    private val _isEmployeeNotFounded = MutableLiveData<Event<Boolean>>()
    val isEmployeeNotFounded = _isEmployeeNotFounded

        fun searchByIIN(iin: String){
            viewModelScope.launch {
                val response = RetrofitClient.getApiService().getEmployeeByIIN(iin)
                when (response.code()) {
                    Constants.SUCCESS_CODE -> _employeeData.postValue(Event(response.body()?.data?.employee!!))
                    Constants.BAD_REQUEST_CODE -> isEmployeeNotFounded.postValue(Event(true))
                }
            }
        }
}