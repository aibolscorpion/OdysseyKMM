package kz.divtech.odyssey.rotation.ui.login.auth.find_employee

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.login.search_by_iin.EmployeeData
import kz.divtech.odyssey.rotation.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        RetrofitClient.getApiService().getEmployeeByPhone(phoneNumber).enqueue(object:
            Callback<EmployeeData> {
            override fun onResponse(call: Call<EmployeeData>, response: Response<EmployeeData>) {
                pBarVisibility.set(View.GONE)
                if(response.code() == Constants.SUCCESS_CODE)
                    _employeeInfo.postValue(Event(response.body()?.data?.employee!!))
                else if(response.code() == Constants.BAD_REQUEST_CODE)
                    _isEmployeeNotFounded.postValue(Event(true))
            }

            override fun onFailure(call: Call<EmployeeData>, t: Throwable) {
                _isErrorHappened.postValue(Event(true))
                pBarVisibility.set(View.GONE)
            }
        })
    }


}