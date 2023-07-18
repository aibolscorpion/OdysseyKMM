package kz.divtech.odyssey.rotation.ui.main

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.SingleTrip
import kz.divtech.odyssey.rotation.domain.repository.EmployeeRepository
import kz.divtech.odyssey.rotation.domain.repository.NotificationRepository
import kz.divtech.odyssey.rotation.domain.repository.OrgInfoRepository
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository

class MainViewModel(private val tripsRepository: TripsRepository,
                    private val employeeRepository: EmployeeRepository,
                    private val notificationRepository: NotificationRepository,
                    private val orgInfoRepository: OrgInfoRepository) : ViewModel() {

    val pBarVisibility = ObservableInt(View.GONE)
    val employeeLiveData: LiveData<Employee> = employeeRepository.employee
    val nearestActiveTrip: LiveData<SingleTrip> = tripsRepository.nearestActiveTrip.asLiveData()
    val threeNotifications: LiveData<List<Notification>> = notificationRepository.notifications.asLiveData()

    fun sendDeviceInfo() = viewModelScope.launch { employeeRepository.sendDeviceInfo() }

    fun getOrgInfoFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            orgInfoRepository.getOrgInfoFromServer()
            pBarVisibility.set(View.GONE)
        }

    fun getNearestActiveTrip() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            tripsRepository.getNearestActiveTrip()
            pBarVisibility.set(View.GONE)
        }

    fun getEmployeeFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            employeeRepository.getAndInstertEmployee()
            pBarVisibility.set(View.GONE)
        }

    fun getNotificationFromFirstPage() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            notificationRepository.getNotificationFromFirstPage()
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