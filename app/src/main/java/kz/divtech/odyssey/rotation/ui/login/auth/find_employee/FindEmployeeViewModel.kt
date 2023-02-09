package kz.divtech.odyssey.rotation.ui.login.auth.find_employee

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.app.Config.BASE_URL_KEY
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.utils.Event
import kz.divtech.odyssey.rotation.utils.SharedPrefs

class FindEmployeeViewModel : ViewModel() {
    private val _isEmployeeNotFounded = MutableLiveData<Event<Boolean>>()
    val isEmployeeNotFounded : LiveData<Event<Boolean>> = _isEmployeeNotFounded

    private val _employeeInfo = MutableLiveData<Event<Employee>>()
    val employeeInfo : LiveData<Event<Employee>> = _employeeInfo

    private val _isErrorHappened = MutableLiveData<Event<Boolean>>()
    val isErrorHappened: LiveData<Event<Boolean>> = _isErrorHappened

    val pBarVisibility = ObservableInt(View.GONE)

    fun getEmployeeInfoByPhoneNumber(phoneNumber: String){
        SharedPrefs.clearUrl(App.appContext)
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            try {
                val response = RetrofitClient.getApiService().getEmployeeByPhone(phoneNumber)
                if(response.code() == Constants.SUCCESS_CODE){
                    val baseUrl = response.headers()[BASE_URL_KEY]!!
                    SharedPrefs.saveUrl(baseUrl, App.appContext)
                    val employee = response.body()?.data?.employee!!
                    _employeeInfo.postValue(Event(employee))
                }else if(response.code() == Constants.BAD_REQUEST_CODE){
                    _isEmployeeNotFounded.postValue(Event(true))
                }
            }catch (e: Exception){
                _isErrorHappened.postValue(Event(true))
            }
            pBarVisibility.set(View.GONE)
        }
    }



}