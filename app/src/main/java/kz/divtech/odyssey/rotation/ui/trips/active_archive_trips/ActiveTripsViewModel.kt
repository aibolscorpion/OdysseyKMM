package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository

class ActiveTripsViewModel(private val tripsRepository: TripsRepository) : ViewModel() {

    suspend fun getActivePagingTrips(): Flow<PagingData<Trip>> {
        return tripsRepository.getActivePagingTrip().cachedIn(viewModelScope)
    }

    suspend fun getArchivePagingTrips(): Flow<PagingData<Trip>> {
        return tripsRepository.getArchivePagingTrip().cachedIn(viewModelScope)
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