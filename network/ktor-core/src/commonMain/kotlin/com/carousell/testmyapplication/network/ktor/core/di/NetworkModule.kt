package com.carousell.testmyapplication.network.ktor.core.di

import com.carousell.testmyapplication.logger.logMessage
import com.carousell.testmyapplication.network.ktor.core.DefaultHeaderProvider
import com.carousell.testmyapplication.network.ktor.core.HeaderProvider
import com.carousell.testmyapplication.network.ktor.core.installRequestInterceptor
import com.carousell.testmyapplication.network.ktor.core.installResponseInterceptor
import com.carousell.testmyapplication.network.ktor.core.serialization.BooleanIntSerializer
import com.carousell.testmyapplication.network.ktor.core.serialization.InstantIso8601Serializer
import com.foundation.preferences.AppDataStore
import com.foundation.preferences.di.PreferencesModule
import dev.skymansandy.wiretap.domain.model.config.http.LogRetention
import io.ktor.client.HttpClient
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import dev.skymansandy.wiretap.plugin.http.WiretapKtorHttpPlugin
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

private fun createHttpClient(
    headerProvider: HeaderProvider,
    appDataStore: AppDataStore,
): HttpClient {
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
        install(WiretapKtorHttpPlugin) {
            enabled = true                                    // default
            shouldLog = { url, method -> true }               // default: log everything
            logRetention = LogRetention.Days(7)
            maxContentLength = 100 * 1024                     // truncate bodies > 100 KB
//            headerAction = { key ->
//                when {
//                    key.equals("Authorization", ignoreCase = true) -> HeaderAction.Mask()
//                    key.equals("Cookie", ignoreCase = true) -> HeaderAction.Skip
//                    else -> HeaderAction.Keep
//                }
//            }
        }
        installRequestInterceptor()
        installResponseInterceptor()

        // Apply dynamic headers from HeaderProvider (suspendable)
        install("HeaderProviderPlugin") {
            requestPipeline.intercept(HttpRequestPipeline.State) {
                headerProvider.getHeaders().forEach { (key, value) ->
                    context.header(key, value)
                }
                proceed()
            }
        }

        engine {
            followRedirects = true
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
                        accessToken = appDataStore.getData(AppDataStore.ACCESS_TOKEN) ?: "",
                        refreshToken = appDataStore.getData(AppDataStore.REFRESH_TOKEN),
                    )
                }

                refreshTokens {
                    tokenRefreshMutex.withLock {
                        logMessage("Authenticating token refresh process...")
                        try {
                            // Execute a manual, isolated authentication refresh call here.
                            // Do NOT use this same client to hit the refresh endpoint directly
                            // unless bypass rules are configured, to avoid infinite 401 loops.

                            appDataStore.updateData(AppDataStore.ACCESS_TOKEN, "Test_1234")
                            appDataStore.updateData(AppDataStore.REFRESH_TOKEN, "Refresh_1234")

                            BearerTokens(
                                appDataStore.getData(AppDataStore.ACCESS_TOKEN) ?: "",
                                appDataStore.getData(AppDataStore.REFRESH_TOKEN),
                            )
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

@Module(includes = [PreferencesModule::class])
@ComponentScan("com.carousell.testmyapplication.network.ktor.core")
class NetworkModule {
    @Single
    fun headerProvider(appDataStore: AppDataStore): HeaderProvider = DefaultHeaderProvider(appDataStore)

    @Single
    fun httpClient(
        headerProvider: HeaderProvider,
        appDataStore: AppDataStore,
    ): HttpClient = createHttpClient(headerProvider, appDataStore)
}
