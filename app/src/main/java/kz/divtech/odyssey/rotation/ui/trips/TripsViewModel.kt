package kz.divtech.odyssey.rotation.ui.trips

import android.annotation.SuppressLint
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository
import kz.divtech.odyssey.rotation.utils.Utils
import java.time.LocalDate

class TripsViewModel(private val tripsRepository: TripsRepository) : ViewModel() {
    val tripsLiveData = tripsRepository.trips.asLiveData()
    private val today = LocalDate.now()

    private val _activeTripsMutableLiveData = MutableLiveData<List<Trip>>()
    val activeTripsLiveData: LiveData<List<Trip>> = _activeTripsMutableLiveData

    private val _archiveTripsMutableLiveData = MutableLiveData<List<Trip>>()
    val archiveTripsLiveData: LiveData<List<Trip>> = _archiveTripsMutableLiveData

    private val activeTrips = mutableListOf<Trip>()
    private val archiveTrips = mutableListOf<Trip>()

    val refreshing = ObservableBoolean()

    fun getTripsFromServer(){
        refreshing.set(true)

        viewModelScope.launch {
            tripsRepository.getTripsFromServer()
            refreshing.set(false)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun divideIntoTwoParts(tripList: List<Trip>){
        archiveTrips.clear()
        activeTrips.clear()
        tripList.forEach { trip ->
            val tripDateTime = Utils.getLocalDateByPattern(trip.date!!)
                if(tripDateTime.isBefore(today)) {
                    archiveTrips.add(trip)
                }
                if(tripDateTime.isAfter(today)) {
                    activeTrips.add(trip)
                }
        }
        sortTrips()
    }

    private fun sortTrips(){
        if(activeTrips.isNotEmpty()){
            activeTrips.sortBy { trip ->
                Utils.getLocalDateByPattern(trip.date!!)
            }
        }
        if(archiveTrips.isNotEmpty()){
            archiveTrips.sortByDescending { trip ->
                Utils.getLocalDateByPattern(trip.date!!)
            }
        }
        _activeTripsMutableLiveData.value = activeTrips
        _archiveTripsMutableLiveData.value = archiveTrips
    }

    class TripsViewModelFactory(private val repository: TripsRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(TripsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return TripsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}