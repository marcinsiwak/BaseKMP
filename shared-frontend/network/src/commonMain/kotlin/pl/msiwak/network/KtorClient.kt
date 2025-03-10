package pl.msiwak.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import pl.msiwak.network.engine.EngineProvider
import pl.msiwak.shared.model.exception.ClientErrorException
import pl.msiwak.shared.model.exception.ServerErrorException

class KtorClient(engine: EngineProvider) {
    val httpClient = HttpClient(engine.getEngine()) {

        if (true) {
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
//                        KermitLogger.i("HTTP Client: $message")
                    }
                }
                sanitizeHeader { header -> header == HttpHeaders.Authorization }
            }
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    encodeDefaults = true
                    ignoreUnknownKeys = true
                }
            )
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
            url("http://192.168.0.162:8080")
            setupCorsHeaders()
//            header(CustomHttpHeaders.PERMISSION_POLICY_HEADER, "fedcm=(self)")
        }

        HttpResponseValidator {
            validateResponse {
//                if (!it.status.isSuccess()) {
//                    KermitLogger.e("HTTP Client Error: ${it.status}")
//                } else {
//                    KermitLogger.v("HTTP Client: ${it.status}")
//                }

                when (val statusCode = it.status.value) {
                    in CLIENT_ERROR_RANGE_START..CLIENT_ERROR_RANGE_END -> throw parseClientErrors(
                        statusCode,
                        it.status.description
                    )

                    in SERVER_ERROR_RANGE_START..SERVER_ERROR_RANGE_END -> throw parseServerErrors(
                        statusCode,
                        it.status.description
                    )
                }
            }
        }
    }

    private fun parseClientErrors(httpCode: Int, httpMessage: String): Throwable =
        when (httpCode) {
            HTTP_BAD_REQUEST -> parseBadRequest(httpCode, httpMessage)
            HTTP_UNPROCESSABLE_ENTITY -> ClientErrorException(httpCode, httpMessage)
            HTTP_FORBIDDEN -> parseForbidden(httpCode, httpMessage)
            HTTP_UNAUTHORIZED -> parseUnauthorized(httpCode, httpMessage)
            else -> ClientErrorException(httpCode, httpMessage)
        }

    private fun parseBadRequest(httpCode: Int, httpMessage: String?): Throwable =
        when (httpMessage) {
            // todo handle errors
            else -> ClientErrorException(httpCode, httpMessage)
        }

    private fun parseForbidden(httpCode: Int, httpMessage: String?): Throwable =
        when (httpMessage) {
            // todo handle errors
            else -> ClientErrorException(httpCode, httpMessage)
        }

    private fun parseUnauthorized(httpCode: Int, httpMessage: String?): Throwable =
        ServerErrorException(httpCode, httpMessage)

    private fun parseServerErrors(httpCode: Int, httpMessage: String?): Throwable =
        ServerErrorException(httpCode, httpMessage)

    private fun DefaultRequest.DefaultRequestBuilder.setupCorsHeaders() = headers {
        append(HttpHeaders.AccessControlAllowHeaders, "Content-Type")
        append(HttpHeaders.AccessControlAllowOrigin, "*")
        append(HttpHeaders.AccessControlAllowMethods, "OPTIONS,POST,GET")
    }

    companion object {
        const val CLIENT_ERROR_RANGE_START = 400
        const val CLIENT_ERROR_RANGE_END = 499
        const val SERVER_ERROR_RANGE_START = 500
        const val SERVER_ERROR_RANGE_END = 599
        const val HTTP_BAD_REQUEST = 400
        const val HTTP_UNPROCESSABLE_ENTITY = 422
        const val HTTP_FORBIDDEN = 403
        const val HTTP_UNAUTHORIZED = 401
    }
}
