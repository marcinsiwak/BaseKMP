package pl.msiwak.shared.model.exception

class ServerErrorException(httpCode: Int, httpMessage: String?) : ApiException(httpCode, httpMessage)
