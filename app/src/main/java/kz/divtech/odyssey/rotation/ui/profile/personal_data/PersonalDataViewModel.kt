package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonalDataViewModel(): ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)

    fun updatePersonalData(employee: Employee){
        pBarVisibility.set(View.VISIBLE)
        RetrofitClient.getApiService().updateData(employee).enqueue(object: Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                pBarVisibility.set(View.GONE)
                if(response.isSuccessful){
                    Toast.makeText(App.appContext, R.string.application_sent, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                pBarVisibility.set(View.GONE)

            }

        })
    }
}