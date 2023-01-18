package kz.divtech.odyssey.rotation.ui.main

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.domain.repository.NotificationRepository
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository
import kz.divtech.odyssey.rotation.utils.Utils
import java.time.LocalDate

class MainViewModel(private val tripsRepository: TripsRepository,
                    private val employeeRepository: EmployeeRepository,
                    private val notificationRepository: NotificationRepository) : ViewModel() {

    val tripsLiveData = tripsRepository.trips.asLiveData()
    val employeeLiveData: LiveData<Employee> = employeeRepository.employee.asLiveData()
    val notificationsLiveData: LiveData<List<Notification>> = notificationRepository.notifications.asLiveData()

    private val _nearestTripMutableLiveData = MutableLiveData<Trip?>()
    val nearestTripLiveData: LiveData<Trip?> = _nearestTripMutableLiveData

    val nearestTripVisibility = ObservableInt(View.GONE)
    val pBarVisibility = ObservableInt(View.GONE)

    private val today = LocalDate.now()

    fun getTripsFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            tripsRepository.getTripsFromServer()
            pBarVisibility.set(View.GONE)
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

    fun getEmployeeFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            employeeRepository.getEmployeeFromServer()
            pBarVisibility.set(View.GONE)
        }


    fun getNotificationsFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            notificationRepository.getNotificationsFromServer()
            pBarVisibility.set(View.GONE)
        }


    class MainViewModelFactory(private val tripsRepository: TripsRepository,
                               private val employeeRepository: EmployeeRepository,
                                private val notificationRepository: NotificationRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(tripsRepository, employeeRepository, notificationRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}