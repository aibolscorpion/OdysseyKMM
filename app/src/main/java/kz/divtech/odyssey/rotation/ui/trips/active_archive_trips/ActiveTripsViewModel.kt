package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import androidx.lifecycle.*
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.SortTripType

class ActiveTripsViewModel(private val tripsRepository: TripsRepository) : ViewModel() {
    val checkedStatusList = mutableListOf<String>()
    var direction = Constants.ALL_DIRECTION

    private var _sortType = MutableLiveData(SortTripType.BY_DEPARTURE_DATE)
    val sortType: LiveData<SortTripType> = _sortType

    private var _appliedFilterCount = MutableLiveData(0)
    val appliedFilterCount: LiveData<Int> = _appliedFilterCount

    fun refreshTrips(isActive: Boolean): Flow<PagingData<Trip>> {
        val sortedTripsFlow = when (_sortType.value) {
            SortTripType.BY_STATUS -> tripsRepository.getTripsSortedByStatus(isActive)
            else -> tripsRepository.getTripsSortedByDate(isActive)
        }

        return if (checkedStatusList.isNotEmpty()) {
                tripsRepository.getFilteredTrips(isActive, checkedStatusList.toTypedArray(), direction)
            } else {
                sortedTripsFlow
            }
    }

    fun resetFilter(){
        direction = Constants.ALL_DIRECTION
        checkedStatusList.clear()
        setAppliedFilterCount(0)
    }

    fun setAppliedFilterCount(count: Int){
        _appliedFilterCount.value = count
    }

    fun increaseByOneFilterCount(){
        _appliedFilterCount.value = _appliedFilterCount.value?.let { it +1 }
    }

    fun setSortType(type: SortTripType){
        _sortType.value = type
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