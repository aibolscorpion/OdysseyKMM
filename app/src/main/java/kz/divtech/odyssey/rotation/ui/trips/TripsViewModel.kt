package kz.divtech.odyssey.rotation.ui.trips

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.Data
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.ApplicationsRepository
import kz.divtech.odyssey.rotation.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class TripsViewModel(private val repository: ApplicationsRepository) : ViewModel() {
    private val _tripsMutableLiveData = MutableLiveData<Data>()
    val tripsLiveData: LiveData<Data> = _tripsMutableLiveData
    private val today = LocalDate.now()
    val activeTrips = ArrayList<Trip>()
    val archiveTrips = ArrayList<Trip>()

    val visibility = ObservableInt(View.GONE)


    fun getTrips(){
            visibility.set(View.VISIBLE)
            RetrofitClient.getApiService().getTrips().enqueue(object: Callback<Data> {
                override fun onResponse(call: Call<Data>, response: Response<Data>) {
                    visibility.set(View.GONE)
                    if(response.isSuccessful){
                        _tripsMutableLiveData.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<Data>, t: Throwable) {
                    visibility.set(View.GONE)
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

    class TripsViewModelFactory(private val repository: ApplicationsRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(TripsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return TripsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    val allTripsFromDatabase: LiveData<Data> = repository.allApplications.asLiveData()

    fun insert(data: Data) = viewModelScope.launch {
        repository.insertApplications(data)
    }


}