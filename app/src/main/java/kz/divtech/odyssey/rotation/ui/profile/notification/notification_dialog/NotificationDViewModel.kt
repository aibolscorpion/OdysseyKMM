package kz.divtech.odyssey.rotation.ui.profile.notification.notification_dialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository

class NotificationDViewModel(private val tripsRepository: TripsRepository) : ViewModel() {

    fun getTripById(id: Int): LiveData<Trip> = tripsRepository.getTripById(id).asLiveData()

    class NotificationDViewModelFactory(private val tripsRepository: TripsRepository): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(NotificationDViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return NotificationDViewModel(tripsRepository) as T
            }
            throw IllegalArgumentException("Unknown viewModel class")
        }
    }

}