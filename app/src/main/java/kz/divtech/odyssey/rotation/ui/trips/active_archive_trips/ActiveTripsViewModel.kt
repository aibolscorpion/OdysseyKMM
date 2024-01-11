package kz.divtech.odyssey.rotation.ui.trips.active_archive_trips

import androidx.lifecycle.*
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.ui.trips.active_archive_trips.dialogs.SortTripType
import kz.divtech.odyssey.shared.domain.model.trips.response.trip.Trip
import kz.divtech.odyssey.shared.domain.repository.TripsRepository
import javax.inject.Inject

@HiltViewModel
class ActiveTripsViewModel @Inject constructor(private val tripsRepository: TripsRepository) : ViewModel() {
    val checkedStatusList = mutableListOf(
        Constants.STATUS_ISSUED, Constants.STATUS_OPENED,
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
        _appliedFilterCount.value = _appliedFilterCount.value?.plus(1)
    }

    fun setSortType(type: SortTripType){
        _sortType.value = type
    }

}