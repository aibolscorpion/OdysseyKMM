package kz.divtech.odyssey.rotation.ui.profile.notification.notification_dialog

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.SingleTrip
import kz.divtech.odyssey.rotation.data.repository.TripsRepository
import kz.divtech.odyssey.rotation.data.remote.result.*
import kz.divtech.odyssey.shared.domain.repository.NotificationsRepository
import javax.inject.Inject

@HiltViewModel
class NotificationDViewModel @Inject constructor(private val tripsRepository: TripsRepository,
                                                 private val notificationRepository: NotificationsRepository
) : ViewModel() {
    private val _tripResult = MutableLiveData<Result<SingleTrip>>()
    val tripResult: LiveData<Result<SingleTrip>> get() = _tripResult

    fun getTripById(id: Int){
        viewModelScope.launch {
           val result = tripsRepository.getTripById(id)
            _tripResult.value = result
        }
    }

    fun markNotificationAsRead(id: String) = viewModelScope.launch {
        notificationRepository.markNotificationAsRead(id)
    }


}