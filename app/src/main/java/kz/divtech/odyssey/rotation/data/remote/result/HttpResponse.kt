package kz.divtech.odyssey.rotation.data.remote.result

import okhttp3.Headers

interface HttpResponse {

    val statusCode: Int

    val statusMessage: String?

    val url: String?

    val headers: Headers?
}