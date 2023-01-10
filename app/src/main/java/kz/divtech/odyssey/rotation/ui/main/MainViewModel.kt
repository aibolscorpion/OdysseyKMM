package kz.divtech.odyssey.rotation.ui.main

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository
import kz.divtech.odyssey.rotation.utils.Utils
import java.time.LocalDate

class MainViewModel(private val tripsRepository: TripsRepository,
                    private val employeeRepository: EmployeeRepository) : ViewModel() {

    val tripsLiveData = tripsRepository.trips.asLiveData()
    val employeeLiveData: LiveData<Employee> = employeeRepository.employee.asLiveData()

    private val _nearestTripMutableLiveData = MutableLiveData<Trip?>()
    val nearestTripLiveData: LiveData<Trip?> = _nearestTripMutableLiveData

    val nearestTripVisibility = ObservableInt(View.GONE)
    val pBarVisibility = ObservableInt(View.GONE)

    private val today = LocalDate.now()

    fun getTripsFromServer(){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            tripsRepository.getTripsFromServer()
            pBarVisibility.set(View.GONE)
        }
    }

    fun findNearestTrip(trips: List<Trip>){
        run loop@{
            trips.forEach{ trip ->
                val tripDateTime = Utils.getLocalDateByPattern(trip.date!!)
                if(tripDateTime.isAfter(today)) {
                    _nearestTripMutableLiveData.postValue(trip)
                    nearestTripVisibility.set(View.VISIBLE)
                    return@loop
                }
            }
        }
    }

    fun getEmployeeFromServer(){
        pBarVisibility.set(View.VISIBLE)
        viewModelScope.launch {
            employeeRepository.getEmployeeFromServer()
            pBarVisibility.set(View.GONE)
        }
    }

    class MainViewModelFactory(private val tripsRepository: TripsRepository,
                               private val employeeRepository: EmployeeRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(tripsRepository, employeeRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}