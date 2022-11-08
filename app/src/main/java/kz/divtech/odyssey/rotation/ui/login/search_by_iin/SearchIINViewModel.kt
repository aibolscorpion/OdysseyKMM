package kz.divtech.odyssey.rotation.ui.login.search_by_iin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.login.search_by_iin.EmployeeData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchIINViewModel : ViewModel() {
    private val _employeeData: MutableLiveData<Employee> = MutableLiveData<Employee>()
    val employeeData: LiveData<Employee> = _employeeData

        fun searchByIIN(iin: String){
            RetrofitClient.getApiService().getEmployeeByIIN(iin).enqueue(object: Callback<EmployeeData> {
                override fun onResponse(call: Call<EmployeeData>, response: Response<EmployeeData>) {
                    if(response.isSuccessful){
                        _employeeData.postValue(response.body()?.data?.employee!!)
                    }
                }

                override fun onFailure(call: Call<EmployeeData>, t: Throwable) {
                }

            })
        }

}