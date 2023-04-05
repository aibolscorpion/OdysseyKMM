package kz.divtech.odyssey.rotation.ui.trips.refund.create_application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.domain.repository.RefundRepository
import okhttp3.ResponseBody

class RefundViewModel(val repository: RefundRepository) : ViewModel() {

     suspend fun sendApplicationToRefund(applicationId: Int, segmentId: IntArray, reason: String):
             Result<Map<String, Int>> {
         return repository.sendApplicationToRefund(applicationId, segmentId, reason)

    }

    suspend fun cancelRefund(refundId: Int): Result<ResponseBody>{
        return repository.cancelRefund(refundId)
    }

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