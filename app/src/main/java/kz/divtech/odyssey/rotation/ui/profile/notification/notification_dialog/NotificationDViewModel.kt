package kz.divtech.odyssey.rotation.ui.profile.notification.notification_dialog

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.NotificationRepository
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository

class NotificationDViewModel(private val tripsRepository: TripsRepository,
            private val notificationRepository: NotificationRepository) : ViewModel() {

    val pBarVisibility = ObservableInt(View.GONE)

    fun getTripById(id: Int): LiveData<Trip> = tripsRepository.getTripById(id).asLiveData()

    fun markNotificationAsRead(id: String) = viewModelScope.launch {
        pBarVisibility.set(View.VISIBLE)
        notificationRepository.markNotificationAsRead(id)
        pBarVisibility.set(View.GONE)
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