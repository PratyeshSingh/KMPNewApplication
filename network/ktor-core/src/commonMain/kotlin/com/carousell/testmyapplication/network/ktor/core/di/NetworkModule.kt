package com.carousell.testmyapplication.network.ktor.core.di

import com.carousell.testmyapplication.logger.logMessage
import com.carousell.testmyapplication.network.ktor.core.DefaultHeaderProvider
import com.carousell.testmyapplication.network.ktor.core.HeaderProvider
import com.carousell.testmyapplication.network.ktor.core.installRequestInterceptor
import com.carousell.testmyapplication.network.ktor.core.installResponseInterceptor
import com.carousell.testmyapplication.network.ktor.core.serialization.BooleanIntSerializer
import com.carousell.testmyapplication.network.ktor.core.serialization.InstantIso8601Serializer
import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single
import kotlin.time.Instant

// Guard to prevent concurrent multiple 401 token refresh API calls
private val tokenRefreshMutex = Mutex()
private const val REQ_TIMEOUT = 60000L

private fun createHttpClient(headerProvider: HeaderProvider): HttpClient {
    val json =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            serializersModule =
                SerializersModule {
                    // Register custom serializers here
                    contextual(Instant::class, InstantIso8601Serializer)
                    contextual(Boolean::class, BooleanIntSerializer)
                }
        }

    return HttpClient {
        installRequestInterceptor()
        installResponseInterceptor()

        engine {
            followRedirects = true
        }

        defaultRequest {
            headerProvider.getHeaders().forEach { (key, value) ->
                header(key, value)
            }
        }

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
                cause is ConnectTimeoutException || cause is SocketTimeoutException
            }
            exponentialDelay(base = 2.0, maxDelayMs = 4000)
        }

        install(HttpTimeout) {
            requestTimeoutMillis = REQ_TIMEOUT
            socketTimeoutMillis = REQ_TIMEOUT
        }

        // 3. AUTHENTICATOR (401 Interceptor with lock protection)
        install(Auth) {
            bearer {
                loadTokens {
                    // Normally loaded from Multiplatform Settings / DataStore
                    BearerTokens(
                        accessToken = "ACCESS_TOKEN_HERE",
                        refreshToken = "REFRESH_TOKEN_HERE",
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
            logger =
                object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        logMessage(message)
                    }
                }
        }
    }
}

@Module
@ComponentScan("com.carousell.testmyapplication.network.ktor.core")
class NetworkModule {
    @Single
    fun headerProvider(): HeaderProvider = DefaultHeaderProvider()

    @Single
    fun httpClient(headerProvider: HeaderProvider): HttpClient = createHttpClient(headerProvider)
}
