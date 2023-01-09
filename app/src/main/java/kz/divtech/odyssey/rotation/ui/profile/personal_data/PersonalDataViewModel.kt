package kz.divtech.odyssey.rotation.ui.profile.personal_data

import android.view.View
import android.widget.Toast
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.R
import kz.divtech.odyssey.rotation.app.App
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import timber.log.Timber

class PersonalDataViewModel(): ViewModel() {
    val pBarVisibility = ObservableInt(View.GONE)

    fun updatePersonalData(employee: Employee){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            try{
                val response = RetrofitClient.getApiService().updateData(employee)
                if(response.isSuccessful){
                    Toast.makeText(App.appContext, R.string.application_sent, Toast.LENGTH_LONG).show()
                }
            }catch (e: Exception){
                Timber.e("exception - ${e.message}")
            }
            pBarVisibility.set(View.GONE)
        }
    }
}