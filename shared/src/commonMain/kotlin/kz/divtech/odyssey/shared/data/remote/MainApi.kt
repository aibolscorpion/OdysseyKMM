package kz.divtech.odyssey.shared.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

object MainApi {
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
             header("deviceId", "someDeviceId")
             header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImEyMDEyZWVlZjJhZTBiOGNiYjNlOTQ1NTA4N2MzNGUxMWU0YjU0MDJkYWNmNjEyYTgxYjZhNzY1NGUzNDA4MzFlNTA5MmZjZDFmM2RhMjM1In0.eyJhdWQiOiIxIiwianRpIjoiYTIwMTJlZWVmMmFlMGI4Y2JiM2U5NDU1MDg3YzM0ZTExZTRiNTQwMmRhY2Y2MTJhODFiNmE3NjU0ZTM0MDgzMWU1MDkyZmNkMWYzZGEyMzUiLCJpYXQiOjE3MDI5MDE3ODgsIm5iZiI6MTcwMjkwMTc4OCwiZXhwIjoxNzAzNTA2NTg4LCJzdWIiOiI0NTk0MiIsInNjb3BlcyI6W119.RZ_zqe_qj7BDvyzCZalk_eUelA8BVu47Na9vu5REXmj5ozsEO2DFAuMNpDSl7mXaZHbg2Rhxit0ryDlzUVdDK13S7m3LQQb5beos-K3iLKrW7k2M92LiTAr0wQy_2Aj-nCrmR7YEWsrNEQcwZfNtUlgB1Ei7_0PplTAeVsmY686IqMWA0I7_uZPwabB-zHl1BuXIf8_k7h2IZSutgK6rs6VRfNr_z3JP4jlzg2FapQ5KqmLh7aEcUYRb0FVZ3tt-_va99nk-fUl0VHFAhUNtfe9iFQtUpNkTbUBCI-Llc06isVYqmLxZ70UFxsuaWhnRyQHeNM1hniB3qvBpIARF27W-RMYf8pr8OoxI7UjdsH3paE03Em9_yhv3xFCZeZIOI4LypPsNQx0K9-vSFQJiVZYNgi5hLfM9JeT_e7CUzc_kDVHWyI7lUKMnlv62zcqmBhm2ye4-ulLxUbHWeXnSLyczorvWZmSMHTCnOv341ZF5HFdqta07pSwac65KreFQI6XI7A94WKFTf8_Mfdaw3g5kAYogrLV1H_3G5Q8m9ZJ1ybDVhhuzUxq4lsH52RuLZiSpXiDheLOdIpyY6KTARw0LA4w1TaD55EnIWg4gIwOm3lFaXaBWCQ86UloQ-20AxVOkArO_e59j7s5o0jc9D9vRZYn1t6e-ycndMWui4U4")
         }
        install(Logging){
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
}