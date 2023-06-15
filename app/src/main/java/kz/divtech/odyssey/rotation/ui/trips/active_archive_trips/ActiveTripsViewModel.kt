package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository

class ActiveTripsViewModel(private val tripsRepository: TripsRepository) : ViewModel() {
    var appliedFilterCount = 0
    val checkedStatusList = mutableListOf<String>()
    var direction = Constants.ALL_DIRECTION

    fun getTripsSortedByDate(isActive: Boolean): Flow<PagingData<Trip>> {
        return tripsRepository.getTripsSortedByDate(isActive).cachedIn(viewModelScope)
    }
    fun getTripsSortedByStatus(isActive: Boolean)
            : Flow<PagingData<Trip>> {
        return tripsRepository.getTripsSortedByStatus(isActive).cachedIn(viewModelScope)
    }

    fun getFilteredTrips(isActive: Boolean): Flow<PagingData<Trip>> {
        return tripsRepository.getFilteredTrips(isActive, checkedStatusList.toTypedArray(), direction).cachedIn(viewModelScope)
    }

    class TripsViewModelFactory(private val repository: TripsRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(ActiveTripsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return ActiveTripsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}