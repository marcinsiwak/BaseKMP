package pl.msiwak.shared.model.exception

abstract class ApiException(val httpCode: Int, val httpMessage: String?) : Exception(httpMessage)
