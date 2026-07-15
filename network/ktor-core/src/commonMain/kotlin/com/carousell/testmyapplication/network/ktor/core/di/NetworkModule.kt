package com.carousell.testmyapplication.network.ktor.core.di

import com.carousell.testmyapplication.logger.logMessage
import com.carousell.testmyapplication.network.ktor.core.AppNetworkClient
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import org.koin.dsl.module

// Guard to prevent concurrent multiple 401 token refresh API calls
private val tokenRefreshMutex = Mutex()

private fun createHttpClient(): HttpClient {
    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    return HttpClient {
        // 1. CONTENT-TYPE OVERRIDE (Serves application/json anyway)
        install(ContentNegotiation) {
            // If API returns plain text but holds valid JSON, parse it as JSON
            json(json, contentType = ContentType.Any)
        }

        // 2. RETRY INTERCEPTOR (Ktor 3 structure)
        install(HttpRequestRetry) {
            maxRetries = 3
            retryOnServerErrors() // Automatically handles 5xx HTTP codes
            retryOnExceptionIf { _, cause ->
                // Recover on socket, network connection timeouts or drops
                cause is io.ktor.client.network.sockets.ConnectTimeoutException ||
                        cause is io.ktor.client.network.sockets.SocketTimeoutException
            }
            exponentialDelay(base = 2.0, maxDelayMs = 4000)
        }

        // 3. AUTHENTICATOR (401 Interceptor with lock protection)
        install(Auth) {
            bearer {
                loadTokens {
                    // Normally loaded from Multiplatform Settings / DataStore
                    BearerTokens(
                        accessToken = "ACCESS_TOKEN_HERE",
                        refreshToken = "REFRESH_TOKEN_HERE"
                    )
                }

                refreshTokens {
                    tokenRefreshMutex.withLock {
                        logMessage("Authenticating token refresh process...")
                        try {
                            // Execute a manual, isolated authentication refresh call here.
                            // Do NOT use this same client to hit the refresh endpoint directly
                            // unless bypass rules are configured, to avoid infinite 401 loops.
                            val freshAccess = "NEW_ACCESS_TOKEN"
                            val freshRefresh = "NEW_REFRESH_TOKEN"

                            BearerTokens(freshAccess, freshRefresh)
                        } catch (e: Exception) {
                            logMessage("Critical auth token refresh failed: ${e.message}")
                            // Triggers application logout broadcast, resets storage here
                            null
                        }
                    }
                }

                // Exclude authorization header if requesting external CDNs
                sendWithoutRequest { request ->
                    request.url.host.contains("your-api-domain.com")
                }
            }
        }

        // 4. DEVELOPMENT LOGGING (Helps diagnose network payloads)
        install(Logging) {
            level = LogLevel.ALL
            logger = object : io.ktor.client.plugins.logging.Logger {
                override fun log(message: String) {
                    logMessage("KTOR: $message")
                }
            }
        }
    }
}

val networkModule = module {
    logMessage("NetworkModule :: STEP1")
    single { AppNetworkClient(createHttpClient()) }
    logMessage("NetworkModule :: STEP2")
}