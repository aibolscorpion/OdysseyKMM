package kz.divtech.odyssey.rotation.ui.trips.refund.application

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.launch
import kz.divtech.odyssey.shared.common.Resource
import kz.divtech.odyssey.shared.domain.model.trips.refund.create.RefundAppResponse
import kz.divtech.odyssey.shared.domain.model.trips.refund.create.RefundApplication
import kz.divtech.odyssey.shared.domain.repository.RefundRepository
import javax.inject.Inject

@HiltViewModel
class RefundViewModel @Inject constructor(val repository: RefundRepository) : ViewModel() {
    private val _sendRefundResult = MutableLiveData<Resource<RefundAppResponse>>()
    val sendRefundResult: LiveData<Resource<RefundAppResponse>> = _sendRefundResult

    private val _cancelRefundResult = MutableLiveData<Resource<HttpResponse>>()
    val cancelRefundResult: LiveData<Resource<HttpResponse>> = _cancelRefundResult

     fun sendApplicationToRefund(applicationId: Int, segmentId: IntArray, reason: String){
         viewModelScope.launch {
             val application = RefundApplication(applicationId, segmentId.toList(), reason)
             _sendRefundResult.value = repository.sendApplicationToRefund(application)
         }
    }

    fun cancelRefund(refundId: Int){
        viewModelScope.launch {
            _cancelRefundResult.value = repository.cancelRefund(refundId)
        }
    }

}