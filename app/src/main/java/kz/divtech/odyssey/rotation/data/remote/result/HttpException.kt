package kz.divtech.odyssey.rotation.data.remote.result

import com.google.gson.Gson
import kz.divtech.odyssey.rotation.common.Constants
import kz.divtech.odyssey.rotation.domain.model.errors.BadRequest
import okhttp3.Headers
import okhttp3.ResponseBody

class HttpException(
    val statusCode: Int,
    val statusMessage: String? = null,
    val url: String? = null,
    cause: Throwable? = null,
    val headers: Headers? = null,
    val errorBody: ResponseBody? = null
) : Exception(null, cause){
    override fun toString(): String {
        return if(statusCode == Constants.BAD_REQUEST_CODE){
            val errorResponse = Gson().fromJson(errorBody?.string(), BadRequest::class.java)
            errorResponse.message
        }else{
            "HttpException: statusCode = $statusCode, statusMessage = $statusMessage"
        }
    }
}