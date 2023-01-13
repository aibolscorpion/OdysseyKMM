package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository

class ActiveTripsViewModel(private val tripsRepository: TripsRepository) : ViewModel() {

    val refreshing = ObservableBoolean()

    fun getTripsFromServer(){
        refreshing.set(true)

        viewModelScope.launch {
            tripsRepository.getTripsFromServer()
            refreshing.set(false)
        }
    }


    class ActiveTripsViewModelFactory(private val tripsRepository: TripsRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ActiveTripsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ActiveTripsViewModel(tripsRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}