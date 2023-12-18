package kz.divtech.odyssey.rotation.ui.main

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.data.local.SharedPrefsManager
import kz.divtech.odyssey.rotation.domain.model.login.login.employee_response.Employee
import kz.divtech.odyssey.rotation.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.SingleTrip
import kz.divtech.odyssey.rotation.data.repository.ProfileRepository
import kz.divtech.odyssey.rotation.data.repository.NotificationRepository
import kz.divtech.odyssey.rotation.data.repository.OrgInfoRepository
import kz.divtech.odyssey.rotation.data.repository.TripsRepository
import kz.divtech.odyssey.rotation.domain.model.DeviceInfo
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val tripsRepository: TripsRepository,
                                        private val profileRepository: ProfileRepository,
                                        private val notificationRepository: NotificationRepository,
                                        private val orgInfoRepository: OrgInfoRepository
) : ViewModel() {

    val pBarVisibility = ObservableInt(View.GONE)
    val employeeLiveData: LiveData<Employee> = profileRepository.employee
    val nearestActiveTrip: LiveData<SingleTrip> = tripsRepository.nearestActiveTrip.asLiveData()
    val threeNotifications: LiveData<List<Notification>> = notificationRepository.notifications.asLiveData()

    fun sendDeviceInfo() = viewModelScope.launch {
        val deviceType = android.os.Build.MANUFACTURER + android.os.Build.MODEL
        val deviceInfo = DeviceInfo(Constants.ANDROID,
                                    deviceType,
            SharedPrefsManager.fetchFirebaseToken())
        profileRepository.sendDeviceInfo(deviceInfo)
    }

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
            profileRepository.getAndInsertProfile()
            pBarVisibility.set(View.GONE)
        }

    fun getNotificationFromFirstPage() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            notificationRepository.getNotificationFromFirstPage()
            pBarVisibility.set(View.GONE)
        }


}