package kz.divtech.odyssey.rotation.ui.trips.refund.application

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.domain.model.trips.Trip
import kz.divtech.odyssey.rotation.domain.model.trips.refund.applications.RefundAppItem
import kz.divtech.odyssey.rotation.domain.repository.RefundRepository
import okhttp3.ResponseBody

class RefundViewModel(val repository: RefundRepository) : ViewModel() {
    private val _refundListMutableLiveData = MutableLiveData<Result<ArrayList<RefundAppItem>>>()
    val refundListLiveData = _refundListMutableLiveData

    fun getRefundListByTripId(applicationId: Int){
        viewModelScope.launch {
            val result = repository.getRefundListByTripId(applicationId)
            _refundListMutableLiveData.postValue(result)
        }
    }

     suspend fun sendApplicationToRefund(applicationId: Int, segmentId: IntArray, reason: String):
             Result<Map<String, Int>> {
         return repository.sendApplicationToRefund(applicationId, segmentId, reason)

    }

    suspend fun cancelRefund(refundId: Int): Result<ResponseBody>{
        return repository.cancelRefund(refundId)
    }

    fun getTripById(segmentId: Int, tripId: Int): LiveData<Trip> =
        repository.getTripById(tripId).asLiveData()

    class RefundViewModelFactory(private val repository: RefundRepository) : ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(RefundViewModel::class.java)){
                @Suppress("UNCHECKED_CAST")
                return RefundViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }


}