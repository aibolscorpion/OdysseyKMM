package kz.divtech.odyssey.rotation.ui.trips

import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.*
import kz.divtech.odyssey.rotation.domain.model.trips.Data
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.repository.ApplicationsRepository
import kz.divtech.odyssey.rotation.utils.Utils
import java.time.LocalDate

class TripsViewModel(private val repository: ApplicationsRepository) : ViewModel() {
    val tripsLiveData: LiveData<Data> = repository.allApplications.asLiveData()
    private val today = LocalDate.now()
    val activeTrips = ArrayList<Trip>()
    val archiveTrips = ArrayList<Trip>()

    val visibility = ObservableInt(View.GONE)

    fun compareTripDatesWithToday(){
        archiveTrips.clear()
        activeTrips.clear()
        tripsLiveData.value?.data?.data?.forEach { trip ->
            val tripDateTime = Utils.getLocalDateByPattern(trip.date!!)
                if(tripDateTime.isBefore(today)) {
                    archiveTrips.add(trip)
                }else {
                    activeTrips.add(trip)
                }
        }
    }

    class TripsViewModelFactory(private val repository: ApplicationsRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(TripsViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return TripsViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}