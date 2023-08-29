package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import androidx.lifecycle.*
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.app.Constants
import kz.divtech.odyssey.rotation.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.SortTripType

class ActiveTripsViewModel(private val tripsRepository: TripsRepository) : ViewModel() {
    val checkedStatusList = mutableListOf(Constants.STATUS_ISSUED, Constants.STATUS_OPENED,
        Constants.STATUS_PARTLY, Constants.STATUS_RETURNED)
    val direction = mutableListOf(Constants.TO_WORK, Constants.TO_HOME)

    private var _sortType = MutableLiveData(SortTripType.BY_DEPARTURE_DATE)
    val sortType: LiveData<SortTripType> = _sortType

    private var _appliedFilterCount = MutableLiveData(0)
    val appliedFilterCount: LiveData<Int> = _appliedFilterCount

    fun getFlowTrips(isActive: Boolean): Flow<PagingData<Trip>> {
        return if(_sortType.value == SortTripType.BY_DEPARTURE_DATE) {
            tripsRepository.getTripsSortedByDate(isActive, checkedStatusList.toTypedArray(),
                direction.toTypedArray())
        }else{
            tripsRepository.getTripsSortedByStatus(
                isActive, checkedStatusList.toTypedArray(), direction.toTypedArray())
        }
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