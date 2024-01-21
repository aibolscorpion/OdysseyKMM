package kz.divtech.odyssey.shared.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kz.divtech.odyssey.shared.common.Config
import kz.divtech.odyssey.shared.data.local.data_store.DataStoreManager
import org.greenrobot.eventbus.EventBus

class MainApi(dataStoreManager: DataStoreManager, eventBus: EventBus) {
     @OptIn(ExperimentalSerializationApi::class)
     val httpClient = HttpClient {
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
                explicitNulls = false
            })
        }
         defaultRequest {
             runBlocking {
                 header(Config.DEVICE_ID_KEY, dataStoreManager.getDeviceId().first())
                 header(Config.AUTHORIZATION_KEY, dataStoreManager.getTokenWithBearer())
             }
         }
        install(Logging){
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

         HttpResponseValidator {
             validateResponse { response ->
                 if(response.status == HttpStatusCode.Unauthorized) {
                     eventBus.post(UnauthorizedEvent)
                 }
             }
         }

     }
}

object UnauthorizedEvent