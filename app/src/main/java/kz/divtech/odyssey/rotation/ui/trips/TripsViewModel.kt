package kz.divtech.odyssey.rotation.ui.trips

import android.annotation.SuppressLint
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.TripsRepository
import kz.divtech.odyssey.rotation.utils.Utils
import java.time.LocalDate
import kotlin.collections.ArrayList

class TripsViewModel(tripsRepository: TripsRepository) : ViewModel() {
    val tripsLiveData = tripsRepository.trips.asLiveData()
    private val today = LocalDate.now()
    val activeTrips = ArrayList<Trip>()
    val archiveTrips = ArrayList<Trip>()

    val pBarVisibility = ObservableInt(View.GONE)

    @SuppressLint("SuspiciousIndentation")
    fun compareTripDatesWithToday(){
        archiveTrips.clear()
        activeTrips.clear()
        tripsLiveData.value?.data?.data?.forEach { trip ->
            val tripDateTime = Utils.getLocalDateByPattern(trip.date!!)
                if(tripDateTime.isBefore(today)) {
                    archiveTrips.add(trip)
                }
                if(tripDateTime.isAfter(today)) {
                    activeTrips.add(trip)
                }
        }
    }

    fun sortTripsByDate(){
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