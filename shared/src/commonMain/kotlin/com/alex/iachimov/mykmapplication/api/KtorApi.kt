package com.alex.iachimov.mykmapplication.api

import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal abstract class KtorApi {

    /**
     * Json serialization configuration
     */
    private val jsonConfiguration = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }

    /**
     * HTTP client configuration.
     * Under the hood Ktor is using 2 different HttpClientEngine. The 1st one for Android and the2nd one for iOS.
     * That's why we had to add 2 different dependencies under iOS and Android source-sets in build.gradle.kts file
     */
    val client = HttpClient {
        install(ContentNegotiation) {
            json(jsonConfiguration)
        }

        /**
         * Setup logging
         */
        install(Logging) {
            logger = Logger.SIMPLE
            level = LogLevel.ALL
        }
    }

    /**
     * Configure API details
     */
    fun HttpRequestBuilder.apiUrl(path: String) {
        url {
            takeFrom("https://dog.ceo")
            path("api", path)
        }
    }
}