package kz.divtech.odyssey.rotation.ui.login.auth.find_employee

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.utils.Event

class FindEmployeeViewModel : ViewModel() {
    private val _isEmployeeNotFounded = MutableLiveData<Event<Boolean>>()
    val isEmployeeNotFounded : LiveData<Event<Boolean>> = _isEmployeeNotFounded

    private val _employeeInfo = MutableLiveData<Event<Employee>>()
    val employeeInfo : LiveData<Event<Employee>> = _employeeInfo

    private val _isErrorHappened = MutableLiveData<Event<Boolean>>()
    val isErrorHappened: LiveData<Event<Boolean>> = _isErrorHappened

    val pBarVisibility = ObservableInt(View.GONE)

    fun getEmployeeInfoByPhoneNumber(phoneNumber: String){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            try {
                val callResult = RetrofitClient.getApiService().getEmployeeByPhone(phoneNumber)
                if(callResult.code() == Constants.SUCCESS_CODE){
                    _employeeInfo.postValue(Event(callResult.body()?.data?.employee!!))
                }else if(callResult.code() == Constants.BAD_REQUEST_CODE){
                    _isEmployeeNotFounded.postValue(Event(true))
                }
            }catch (e: Exception){
                _isErrorHappened.postValue(Event(true))
            }
            pBarVisibility.set(View.GONE)
        }
    }



}