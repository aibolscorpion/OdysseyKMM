package kz.divtech.odyssey.rotation.ui.trips

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.Data
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class TripsViewModel : ViewModel() {
    private val _tripsMutableLiveData = MutableLiveData<Data>()
    val tripsLiveData: LiveData<Data> = _tripsMutableLiveData

    private val today = LocalDate.now()
    val activeTrips = mutableListOf<Trip>()
    val archiveTrips = ArrayList<Trip>()

    val visibility = ObservableBoolean()


    fun getTrips(){
            visibility.set(true)
            RetrofitClient.getApiService().getTrips().enqueue(object: Callback<Data> {
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    if(response.isSuccessful){
                        visibility.set(false)
                        _tripsMutableLiveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    visibility.set(false)
                }

            })
    }

    fun compareTripDatesWithToday(){
        archiveTrips.clear()
        activeTrips.clear()
        _tripsMutableLiveData.value?.data?.data?.forEach { trip ->
            val tripDateTime = Utils.getLocalDateByPattern(trip.date!!)
                if(tripDateTime.isBefore(today)) {
                    archiveTrips.add(trip)
                }else {
                    activeTrips.add(trip)
                }
        }
    }

}