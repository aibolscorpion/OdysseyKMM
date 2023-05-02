package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository

class ActiveTripsViewModel(private val tripsRepository: TripsRepository) : ViewModel() {
    var appliedFilterCount = 0
    val checkedStatusList = mutableListOf<String>()
    val directionList = mutableListOf<String>()

    suspend fun getActiveTripsSortedByDate(): Flow<PagingData<Trip>> {
        return tripsRepository.getActiveTripsSortedByDate().cachedIn(viewModelScope)
    }

    suspend fun getArchiveTripsSortedByDate(): Flow<PagingData<Trip>> {
        return tripsRepository.getArchiveTripsSortedByDate().cachedIn(viewModelScope)
    }

    suspend fun getActiveTripsSortedByStatus()
            : Flow<PagingData<Trip>> {
        return tripsRepository.getActiveTripsSortedByStatus().cachedIn(viewModelScope)
    }

    suspend fun getArchiveTripsSortedByStatus()
            : Flow<PagingData<Trip>> {
        return tripsRepository.getArchiveTripsSortedByStatus().cachedIn(viewModelScope)
    }

    suspend fun getFilteredActiveTrips(): Flow<PagingData<Trip>> {
        return tripsRepository.getFilteredActiveTrips(checkedStatusList.toTypedArray(),
            directionList.toTypedArray()).cachedIn(viewModelScope)
    }

    suspend fun getFilteredArchiveTrips(): Flow<PagingData<Trip>> {
        return tripsRepository.getFilteredArchiveTrips(checkedStatusList.toTypedArray(),
            directionList.toTypedArray()).cachedIn(viewModelScope)
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