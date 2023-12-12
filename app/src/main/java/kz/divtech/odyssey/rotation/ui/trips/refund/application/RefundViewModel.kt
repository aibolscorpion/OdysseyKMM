package kz.divtech.odyssey.rotation.ui.trips.refund.application

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.repository.RefundRepository
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class RefundViewModel @Inject constructor(val repository: RefundRepository) : ViewModel() {
    private val _sendRefundResult = MutableLiveData<Result<Map<String, Int>>>()
    val sendRefundResult: LiveData<Result<Map<String, Int>>> = _sendRefundResult

    private val _cancelRefundResult = MutableLiveData<Result<ResponseBody>>()
    val cancelRefundResult: LiveData<Result<ResponseBody>> = _cancelRefundResult

     fun sendApplicationToRefund(applicationId: Int, segmentId: IntArray, reason: String){
         viewModelScope.launch {
             _sendRefundResult.value = repository.sendApplicationToRefund(applicationId, segmentId, reason)
         }
    }

    fun cancelRefund(refundId: Int){
        viewModelScope.launch {
            _cancelRefundResult.value = repository.cancelRefund(refundId)
        }
    }

}