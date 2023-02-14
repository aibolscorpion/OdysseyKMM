package kz.divtech.odyssey.rotation.data.remote.result

import okhttp3.Headers

class HttpException(
    val statusCode: Int,
    val statusMessage: String? = null,
    val url: String? = null,
    cause: Throwable? = null,
    val headers: Headers? = null
) : Exception(null, cause)