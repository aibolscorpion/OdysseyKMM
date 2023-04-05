package kz.divtech.odyssey.rotation.domain.repository

import kz.divtech.odyssey.rotation.data.remote.result.Result
import kz.divtech.odyssey.rotation.data.remote.retrofit.RetrofitClient
import kz.divtech.odyssey.rotation.domain.model.trips.RefundApplication
import okhttp3.ResponseBody

class RefundRepository {

    suspend fun sendApplicationToRefund(applicationId: Int, segmentId: IntArray, reason: String)
            : Result<Map<String, Int>> {
        val application = RefundApplication(applicationId, segmentId.toList(), reason)
        return RetrofitClient.getApiService().sendApplicationToRefund(application)
    }

    suspend fun cancelRefund(refundId: Int): Result<ResponseBody>{
        return RetrofitClient.getApiService().cancelRefund(refundId)
    }
}