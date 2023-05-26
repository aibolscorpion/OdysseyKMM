package kz.divtech.odyssey.rotation.data.remote.result

import okhttp3.Headers
import okhttp3.ResponseBody

class HttpException(
    val statusCode: Int,
    val statusMessage: String? = null,
    val url: String? = null,
    cause: Throwable? = null,
    val headers: Headers? = null,
    val errorBody: ResponseBody? = null
) : Exception(null, cause)