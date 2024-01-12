package kz.divtech.odyssey.rotation.ui.main

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.data.local.SharedPrefsManager
import kz.divtech.odyssey.shared.domain.model.DeviceInfo
import kz.divtech.odyssey.shared.domain.model.profile.Profile
import kz.divtech.odyssey.shared.domain.model.profile.notifications.Notification
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.shared.domain.repository.NotificationsRepository
import kz.divtech.odyssey.shared.domain.repository.OrgInfoRepository
import kz.divtech.odyssey.shared.domain.repository.ProfileRepository
import kz.divtech.odyssey.shared.domain.repository.TripsRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val tripsRepository: TripsRepository,
                                        private val profileRepository: ProfileRepository,
                                        private val notificationRepository: NotificationsRepository,
                                        private val orgInfoRepository: OrgInfoRepository
) : ViewModel() {

    val pBarVisibility = ObservableInt(View.GONE)
    val employeeLiveData: LiveData<Profile?> = profileRepository.getProfileFromDb().asLiveData()
    suspend fun getThreeNotificationsFromDB(): LiveData<List<Notification>> =
        notificationRepository.getFirstThreeNotificationsFromBD().asLiveData()

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
            orgInfoRepository.getOrgInfo()
            pBarVisibility.set(View.GONE)
        }

    fun getNearestActiveTrip() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            tripsRepository.getNearestActiveTrip()
            pBarVisibility.set(View.GONE)
        }

    suspend fun getNearestActiveTripFromDb(): LiveData<Trip?> =
        tripsRepository.getNearestTripFromBd().asLiveData()

    fun getProfileFromServer() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            profileRepository.getProfile()
            pBarVisibility.set(View.GONE)
        }

    fun getNotificationFromFirstPage() =
        viewModelScope.launch {
            pBarVisibility.set(View.VISIBLE)
            notificationRepository.getNotificationsFirstPage()
            pBarVisibility.set(View.GONE)
        }


}