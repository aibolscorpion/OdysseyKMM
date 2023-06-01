package kz.divtech.odyssey.rotation.ui.profile.notification.notification_dialog

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.SingleTrip
import kz.divtech.odyssey.rotation.domain.repository.NotificationRepository
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository
import kz.divtech.odyssey.rotation.data.remote.result.*

class NotificationDViewModel(private val tripsRepository: TripsRepository,
            private val notificationRepository: NotificationRepository) : ViewModel() {
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

    class NotificationDViewModelFactory(private val tripsRepository: TripsRepository,
        private val notificationRepository: NotificationRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NotificationDViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return NotificationDViewModel(tripsRepository, notificationRepository) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }

}