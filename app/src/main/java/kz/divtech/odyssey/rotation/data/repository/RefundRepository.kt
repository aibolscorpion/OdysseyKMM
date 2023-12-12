package kz.divtech.odyssey.rotation.data.repository

import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.remote.retrofit.ApiService
import kz.divtech.odyssey.rotation.domain.model.trips.refund.create.RefundApplication
import okhttp3.ResponseBody

class RefundRepository(private val apiService: ApiService) {
    suspend fun sendApplicationToRefund(applicationId: Int, segmentId: IntArray, reason: String)
            : Result<Map<String, Int>> {
        val application = RefundApplication(applicationId, segmentId.toList(), reason)
        return apiService.sendApplicationToRefund(application)
    }

    suspend fun cancelRefund(refundId: Int): Result<ResponseBody>{
        return apiService.cancelRefund(refundId)
    }


}