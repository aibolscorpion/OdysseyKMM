package kz.divtech.odyssey.rotation.ui.main

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.OrgInfo
import kz.divtech.odyssey.rotation.domain.model.login.login.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.domain.repository.NotificationRepository
import kz.divtech.odyssey.rotation.domain.repository.OrgInfoRepository
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository

class MainViewModel(private val tripsRepository: TripsRepository,
                    private val employeeRepository: EmployeeRepository,
                    private val notificationRepository: NotificationRepository,
                    orgInfoRepository: OrgInfoRepository) : ViewModel() {

    val pBarVisibility = ObservableInt(View.GONE)
    val employeeLiveData: LiveData<Employee> = employeeRepository.employee.asLiveData()
    val threeNotifications: LiveData<List<Notification>> = notificationRepository.notifications.asLiveData()
    val nearestActiveTrip: LiveData<Trip> = tripsRepository.nearestActiveTrip.asLiveData()
    val orgInfo: LiveData<OrgInfo> = orgInfoRepository.orgInfo.asLiveData()

    fun sendDeviceInfo() = viewModelScope.launch { employeeRepository.sendDeviceInfo() }

    fun getTripsFromFirstPage() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            tripsRepository.getTripsFromFirstPage()
            pBarVisibility.set(View.GONE)
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
                                private val notificationRepository: NotificationRepository,
                                private val orgInfoRepository: OrgInfoRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(tripsRepository, employeeRepository, notificationRepository, orgInfoRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}