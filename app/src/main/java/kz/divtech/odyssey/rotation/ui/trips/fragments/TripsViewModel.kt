package kz.divtech.odyssey.rotation.ui.trips.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.Data
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TripsViewModel : ObservableViewModel() {
    private val _tripsMutableLiveData = MutableLiveData<Data>()
    val tripsMutableLiveData: LiveData<Data> = _tripsMutableLiveData

    private val _visibility = MutableLiveData<Boolean>()
    val visibility: LiveData<Boolean> = _visibility


    fun getTrips(){
        _visibility.postValue(true)
        RetrofitClient.getApiService().getTrips().enqueue(object: Callback<Data>{
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                if(response.isSuccessful){
                    _visibility.postValue(false)
                    _tripsMutableLiveData.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                _visibility.postValue(false)
            }

        })
    }
}