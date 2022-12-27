package kz.divtech.odyssey.rotation.ui.main

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.trips.Data
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.ApplicationsRepository
import kz.divtech.odyssey.rotation.utils.Utils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate

class MainFragmentViewModel(private val repository: ApplicationsRepository) : ViewModel() {

    class MainViewModelFactory(private val repository: ApplicationsRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainFragmentViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainFragmentViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    private val _nearestTripMutableLiveData = MutableLiveData<Trip?>()
    val nearestTripLiveData: LiveData<Trip?> = _nearestTripMutableLiveData
    val nearestTripVisibility = ObservableInt(View.VISIBLE)

    private val today = LocalDate.now()

    val pBarVisibility = ObservableInt(View.GONE)

    val employee: LiveData<Employee> = repository.employee.asLiveData()

    fun insertTrips(data: Data) = viewModelScope.launch {
            repository.insertData(data)
    }

    fun getTrips(){
        pBarVisibility.set(View.VISIBLE)
        RetrofitClient.getApiService().getTrips().enqueue(object: Callback<Data> {
            override fun onResponse(call: Call<Data>, response: Response<Data>) {
                pBarVisibility.set(View.GONE)
                val trips = response.body()?.data?.data
                if(response.isSuccessful){
                    insertTrips(response.body()!!)
                    if(trips == null || trips.isEmpty()){
                        nearestTripVisibility.set(View.GONE)
                    }else{
                        findNearestTrip(trips)
                    }
                }
            }

            override fun onFailure(call: Call<Data>, t: Throwable) {
                pBarVisibility.set(View.GONE)
            }
        })
    }

    fun findNearestTrip(trips: List<Trip>){
        run loop@{
            trips.forEach{ trip ->
                val tripDateTime = Utils.getLocalDateByPattern(trip.date!!)
                if(tripDateTime.isAfter(today)) {
                    _nearestTripMutableLiveData.postValue(trip)
                    return@loop
                }
            }
        }
    }

}