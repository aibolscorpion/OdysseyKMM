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
             header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImNiZTg2NDk5ZmIzZTE4ZjYzODU1MzY1YTk3YjJkYzRiMmY3YWQxZTRhMmU2MmFjZjk0NmVhMTFkN2I1OTQ3ZjlmMDViZGNhMDIyZGM4M2ZkIn0.eyJhdWQiOiIxIiwianRpIjoiY2JlODY0OTlmYjNlMThmNjM4NTUzNjVhOTdiMmRjNGIyZjdhZDFlNGEyZTYyYWNmOTQ2ZWExMWQ3YjU5NDdmOWYwNWJkY2EwMjJkYzgzZmQiLCJpYXQiOjE3MDMxNDA4MjYsIm5iZiI6MTcwMzE0MDgyNiwiZXhwIjoxNzAzNzQ1NjI1LCJzdWIiOiI0NTk0MiIsInNjb3BlcyI6W119.SRhGgiRyOq5bwdxSJMndu3VH5psQKuMGSCYCJwTwZCWJi3yuZg_W3Gs0M9CECpj-fLOeyE6qD0znx_OhWQSWc3NLob-e52GkuoWVKn1Ir4xImXU3AyFm-a1NEowVdJA-6iz4ScbQVXQxMzFqfK4STcBX9u83uc7UesCw07XjkLIFV-G0boHerSQq01haEFLsBEeNfE2ZGuv9aUBq09NntJSD5DaIHr2PC3wgV7RqwdAGlFqx7kuq4HDYboHJbWW1Si1gqrxTxw6RXoYDR8nlDOH2GzPFFHFdYfshDsvzSRqVVMVf8WIZ6Ycv0c6APX6wN6BL1gxNune_Et57Bb3BtAu6qxO2d5kV3OFbVQIFlewB_zWvsVZQ8ZN_Aj0WDFcBh1VLlLIloyMkdMjVXzw8b0VSQfyigj9LgCpakIclqgo91CjIE0M-4eV687MJpA2Aijt6LQHHBPVY9FZ8ID230V5bAehcqWzjWqzCJYJ6n46JUlpcyBGNo7NcVTLG9NAdgvvhUBbaHPoMZjxushDqgaeRC7zkrqI8IBHpLCAAy88vU2DGqyob0JydlZKiBR0yTAU39BqDbsm4A5motKLXiXNDh0z6bRJR4m5XxCAsb3fpFhHeMzptp84CKep703X7tBUovmjzgFqqh4267UWLVZbAXjDgwAEh0GN-schlLwk")
         }
        install(Logging){
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
}