package kz.divtech.odyssey.rotation.ui.main

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.Data
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.model.trips.Trips
import kz.divtech.odyssey.rotation.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class MainFragmentViewModel : ViewModel() {
    private val _nearestTripMutableLiveData = MutableLiveData<Trip>()
    val nearestTripLiveData: LiveData<Trip> = _nearestTripMutableLiveData
    private val today = LocalDate.now()

    val name: String = "Aibol"
    val surname: String = "Onggarov"
    val patronymic: String = "Turekhanovich"
    val orgName: String = "Test Organization"

    val visibility = ObservableInt(View.GONE)

    fun getTrips(){
        visibility.set(View.VISIBLE)
        RetrofitClient.getApiService().getTrips().enqueue(object: Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                visibility.set(View.GONE)
                if(response.isSuccessful){
                    val trips = response.body()?.data!!
                    findNearestTrip(trips)
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                visibility.set(View.GONE)
            }

        })
    }

    fun findNearestTrip(trips: Trips){
        run loop@{
            trips.data?.forEach{ trip ->
                val tripDateTime = Utils.getLocalDateByPattern(trip.date!!)
                if(tripDateTime.isAfter(today)) {
                    _nearestTripMutableLiveData.postValue(trip)
                    return@loop
                }
            }
        }
    }

}