package kz.divtech.odyssey.rotation.ui.login.search_by_iin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.GsonBuilder
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.BadRequest
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.login.search_by_iin.EmployeeData
import kz.divtech.odyssey.rotation.utils.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SearchIINViewModel : ViewModel() {
    private val _employeeData = MutableLiveData<Event<Employee>>()
    val employeeData: LiveData<Event<Employee>> = _employeeData

    private val _isEmployeeNotFounded = MutableLiveData<Event<Boolean>>()
    val isEmployeeNotFounded = _isEmployeeNotFounded

        fun searchByIIN(iin: String){
            RetrofitClient.getApiService().getEmployeeByIIN(iin).enqueue(object: Callback<EmployeeData> {
                override fun onResponse(call: Call<EmployeeData>, response: Response<EmployeeData>) {
                    when (response.code()) {
                        200 -> _employeeData.postValue(Event(response.body()?.data?.employee!!))
                        400 -> {
                            lateinit var mError: BadRequest
                            try {
                                mError = GsonBuilder().create().fromJson(
                                    response.errorBody()!!.string(),
                                    BadRequest::class.java
                                )
                            } catch (_: IOException) {
                            }
                            if (mError.slug.equals("employee_not_found"))
                                isEmployeeNotFounded.postValue(Event(true))
                        }
                    }
                }
                override fun onFailure(call: Call<EmployeeData>, t: Throwable) {

                }
            })
        }

}