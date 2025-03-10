package pl.msiwak.shared.model.exception

class ClientErrorException(httpCode: Int, httpMessage: String?) : ApiException(httpCode, httpMessage)
